package com.aisl.shop.service.order;

import com.aisl.shop.dto.request.order.OrderCreateRequest;
import com.aisl.shop.dto.request.order.OrderPayRequest;
import com.aisl.shop.dto.response.order.OrderDetailResponse;
import com.aisl.shop.dto.response.order.OrderListItemResponse;
import com.aisl.shop.entity.Order;
import com.aisl.shop.entity.Order.OrderStatus;
import com.aisl.shop.entity.Order.PaymentMethod;
import com.aisl.shop.entity.OrderItem;
import com.aisl.shop.entity.Product;
import com.aisl.shop.entity.ProductOption;
import com.aisl.shop.enums.OrderItemStatus;
import com.aisl.shop.exception.order.*;
import com.aisl.shop.exception.product.ProductNotFoundException;
import com.aisl.shop.repository.OrderItemRepository;
import com.aisl.shop.repository.OrderRepository;
import com.aisl.shop.repository.ProductOptionRepository;
import com.aisl.shop.repository.ProductRepository;
import com.aisl.shop.util.DiscountCalculator;
import com.aisl.shop.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;

    public Long createOrder(OrderCreateRequest request) {
        // 1. 결제 방식 검증
        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.valueOf(request.getPaymentMethod());
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderStatusException("유효하지 않은 결제 방식입니다.");
        }

        // 2. 주문 객체 초기 생성 (일단 totalPrice = 0)
        Order order = Order.builder()
                .userId(SecurityUtil.getCurrentUserId())
                .email(request.getEmail())
                .name(request.getName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .paymentMethod(paymentMethod)
                .totalPrice(0)
                .status(OrderStatus.PENDING)
                .build();

        int totalPrice = 0;
        List<OrderItem> items = new ArrayList<>();

        // 3. 각 주문 항목 처리
        for (var dto : request.getItems()) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));

            boolean hasOptions = productOptionRepository.existsByProduct_Id(product.getId());
            Long optionId = dto.getOptionId(); // nullable
            String optionName = null;

            // 4. 할인 가격 계산
            Integer discountPrice = DiscountCalculator.calculateDiscountPrice(
                    product.getPrice(),
                    product.getDiscountRate()
            );
            int unitPrice = (discountPrice != null) ? discountPrice : product.getPrice();

            // 5. 옵션 처리
            if (hasOptions && optionId != null) {
                final Long finalOptionId = optionId;
                ProductOption option = productOptionRepository.findById(finalOptionId)
                        .orElseThrow(() ->
                                new ProductOptionNotFoundException("해당 옵션을 찾을 수 없습니다. ID: " + finalOptionId));
                optionName = option.getColor(); // 또는 getSize()
            } else {
                optionId = null; // 옵션 없음 처리
            }

            int quantity = dto.getQuantity();
            int itemTotalPrice = unitPrice * quantity;
            totalPrice += itemTotalPrice;

            // 6. 주문 항목(OrderItem) 생성
            OrderItem item = OrderItem.builder()
                    .order(order)
                    .productId(product.getId())
                    .optionId(optionId)
                    .productName(product.getName())
                    .optionName(optionName)
                    .quantity(quantity)
                    .unitPrice(unitPrice)
                    .totalPrice(itemTotalPrice)
                    .status(OrderItemStatus.PAID)
                    .build();

            items.add(item);
        }

        // 7. 주문 정보 최종 반영 및 저장
        order.setItems(items);
        order.setTotalPrice(totalPrice);
        orderRepository.save(order); // Cascade로 OrderItem도 저장된다고 가정

        return order.getId();
    }



    public List<OrderListItemResponse> getOrderList(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(order -> OrderListItemResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .build()).collect(Collectors.toList());
    }

    public List<OrderListItemResponse> getFilteredOrderList(Long userId, String status, LocalDate startDate, LocalDate endDate) {
        // 🔹 주문 상태 파싱
        OrderStatus enumStatus = null;
        if (status != null) {
            try {
                enumStatus = OrderStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidOrderStatusException("유효하지 않은 주문 상태입니다.");
            }
        }

        // 🔹 날짜 보정 (endDate 포함되도록 하루 뒤 자정 직전까지 확장)
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.plusDays(1).atStartOfDay().minusNanos(1) : null;

        // 🔹 조건 적용된 주문 조회
        List<Order> orders = orderRepository.findByFilters(userId, enumStatus, startDateTime, endDateTime);

        // 🔹 응답 DTO 변환
        return orders.stream()
                .map(order -> OrderListItemResponse.builder()
                        .orderId(order.getId())
                        .status(order.getStatus())
                        .totalPrice(order.getTotalPrice())
                        .createdAt(order.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }


    public OrderDetailResponse getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));

        return OrderDetailResponse.builder()
                .orderId(order.getId())
                .email(order.getEmail())
                .name(order.getName())
                .phone(order.getPhone())
                .address(order.getAddress())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .items(order.getItems().stream().map(item ->
                        OrderDetailResponse.OrderItemResponse.builder()
                                .productName("상품명") // TODO: 상품명 연결
                                .optionName("옵션명") // TODO: 옵션명 연결
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .totalPrice(item.getTotalPrice())
                                .build()
                ).collect(Collectors.toList()))
                .build();
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException("결제 전 주문만 취소할 수 있습니다.");
        }

        order.setStatus(OrderStatus.CANCELLED);
    }

    public void payOrder(Long orderId, OrderPayRequest request) {
        if (!request.isMock()) {
            throw new UnsupportedPaymentException("실제 결제는 지원하지 않습니다.");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException("이미 결제되었거나 취소된 주문입니다.");
        }

        order.setStatus(OrderStatus.PAID);
    }

    public void confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));

        if (order.getStatus() != OrderStatus.PAID) {
            throw new InvalidOrderStatusException("결제 완료된 주문만 구매확정할 수 있습니다.");
        }

        order.setStatus(OrderStatus.COMPLETED);
    }

    public void cancelOrderItem(Long itemId) {
        OrderItem item = getOrderItem(itemId);
        System.out.println("현재 상태: " + item.getStatus()); // 🔍 로그 확인용

        if (item.getStatus() == OrderItemStatus.COMPLETED) {
            throw new InvalidOrderStatusException("구매 확정된 상품은 취소할 수 없습니다.");
        }

        item.setStatus(OrderItemStatus.CANCELLED);
    }


    public void returnOrderItem(Long itemId) {
        OrderItem item = getOrderItem(itemId);
        System.out.println("현재 상태: " + item.getStatus());

        if (item.getStatus() == OrderItemStatus.COMPLETED ||
                item.getStatus() == OrderItemStatus.CANCELLED) {
            throw new InvalidOrderStatusException("취소되었거나 구매 확정된 상품은 반품할 수 없습니다.");
        }

        item.setStatus(OrderItemStatus.RETURN_REQUESTED);
    }



    public void exchangeOrderItem(Long itemId) {
        OrderItem item = getOrderItem(itemId);
        System.out.println("현재 상태: " + item.getStatus());

        if (item.getStatus() == OrderItemStatus.COMPLETED ||
                item.getStatus() == OrderItemStatus.CANCELLED) {
            throw new InvalidOrderStatusException("취소되었거나 구매 확정된 상품은 교환할 수 없습니다.");
        }


        item.setStatus(OrderItemStatus.EXCHANGE_REQUESTED);
    }



    private OrderItem getOrderItem(Long itemId) {
        return orderItemRepository.findById(itemId)
                .orElseThrow(() -> new OrderItemNotFoundException("주문 상품을 찾을 수 없습니다."));
    }
}
