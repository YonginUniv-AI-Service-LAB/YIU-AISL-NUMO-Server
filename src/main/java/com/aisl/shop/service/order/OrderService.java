package com.aisl.shop.service.order;
import com.aisl.shop.dto.request.order.OrderCreateRequest;
import com.aisl.shop.dto.request.order.OrderPayRequest;
import com.aisl.shop.dto.response.order.OrderDetailResponse;
import com.aisl.shop.dto.response.order.OrderListItemResponse;
import com.aisl.shop.entity.*;
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
        Order.PaymentMethod paymentMethod;
        try {
            paymentMethod = Order.PaymentMethod.valueOf(request.getPaymentMethod());
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderStatusException("유효하지 않은 결제 방식입니다.");
        }
        Order order = Order.builder()
                .userId(SecurityUtil.getCurrentUserId())
                .email(request.getEmail())
                .name(request.getName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .paymentMethod(paymentMethod)
                .totalPrice(0)
                .status(Order.OrderStatus.PENDING)
                .build();
        int totalPrice = 0;
        List<OrderItem> items = new ArrayList<>();
        for (var dto : request.getItems()) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));
            boolean hasOptions = productOptionRepository.existsByProduct_Id(product.getId());
            Long optionId = dto.getOptionId();
            String optionName = null;
            Integer discountPrice = DiscountCalculator.calculateDiscountPrice(
                    product.getPrice(),
                    product.getDiscountRate()
            );
            int unitPrice = (discountPrice != null) ? discountPrice : product.getPrice();
            if (hasOptions && optionId != null) {
                final Long finalOptionId = optionId;
                ProductOption option = productOptionRepository.findById(finalOptionId)
                        .orElseThrow(() -> new ProductOptionNotFoundException("해당 옵션을 찾을 수 없습니다. ID: " + finalOptionId));
                optionName = option.getColor();
            } else {
                optionId = null;
            }
            int quantity = dto.getQuantity();
            int itemTotalPrice = unitPrice * quantity;
            totalPrice += itemTotalPrice;
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
        order.setItems(items);
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
        return order.getId();
    }
    // :별:️ 주문 목록 응답에 items(상품명, 옵션명, 수량)를 포함하도록 수정
    public List<OrderListItemResponse> getOrderList(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(order -> OrderListItemResponse.builder()
                        .orderId(order.getId())
                        .status(order.getStatus())
                        .totalPrice(order.getTotalPrice())
                        .createdAt(order.getCreatedAt())
                        .items(order.getItems().stream()
                                .map(item -> OrderListItemResponse.ItemSummary.builder()
                                        .productName(item.getProductName())
                                        .optionName(item.getOptionName())
                                        .quantity(item.getQuantity())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
    // :별:️ 필터 주문 목록 응답도 동일하게 items 포함
    public List<OrderListItemResponse> getFilteredOrderList(Long userId, String status, LocalDate startDate, LocalDate endDate) {
        Order.OrderStatus enumStatus = null;
        if (status != null) {
            try {
                enumStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidOrderStatusException("유효하지 않은 주문 상태입니다.");
            }
        }
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.plusDays(1).atStartOfDay().minusNanos(1) : null;
        return orderRepository.findByFilters(userId, enumStatus, startDateTime, endDateTime).stream()
                .map(order -> OrderListItemResponse.builder()
                        .orderId(order.getId())
                        .status(order.getStatus())
                        .totalPrice(order.getTotalPrice())
                        .createdAt(order.getCreatedAt())
                        .items(order.getItems().stream()
                                .map(item -> OrderListItemResponse.ItemSummary.builder()
                                        .productName(item.getProductName())
                                        .optionName(item.getOptionName())
                                        .quantity(item.getQuantity())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
    public OrderDetailResponse getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("해당 주문을 찾을 수 없습니다. ID: " + orderId));
        return toOrderDetailResponse(order);
    }
    public OrderDetailResponse toOrderDetailResponse(Order order) {
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
                .items(order.getItems().stream().map(item -> {
                    String productName = productRepository.findById(item.getProductId())
                            .map(Product::getName)
                            .orElse("알 수 없음");
                    String optionName = item.getOptionId() != null ?
                            productOptionRepository.findById(item.getOptionId())
                                    .map(option -> {
                                        String sizes = option.getSizes().stream()
                                                .map(ProductSize::getSize)
                                                .collect(Collectors.joining(", "));
                                        return option.getColor() + " / " + sizes;
                                    })
                                    .orElse("옵션 없음")
                            : "옵션 없음";
                    return OrderDetailResponse.OrderItemResponse.builder()
                            .productName(productName)
                            .optionName(optionName)
                            .quantity(item.getQuantity())
                            .unitPrice(item.getUnitPrice())
                            .totalPrice(item.getTotalPrice())
                            .build();
                }).collect(Collectors.toList()))
                .build();
    }
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new InvalidOrderStatusException("결제 전 주문만 취소할 수 있습니다.");
        }
        order.setStatus(Order.OrderStatus.CANCELLED);
    }
    public void payOrder(Long orderId, OrderPayRequest request) {
        if (!request.isMock()) {
            throw new UnsupportedPaymentException("실제 결제는 지원하지 않습니다.");
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new InvalidOrderStatusException("이미 결제되었거나 취소된 주문입니다.");
        }
        order.setStatus(Order.OrderStatus.PAID);
    }
    public void confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));
        if (order.getStatus() != Order.OrderStatus.PAID) {
            throw new InvalidOrderStatusException("결제 완료된 주문만 구매확정할 수 있습니다.");
        }
        order.setStatus(Order.OrderStatus.COMPLETED);
    }
    public void cancelOrderItem(Long itemId) {
        OrderItem item = getOrderItem(itemId);
        if (item.getStatus() == OrderItemStatus.COMPLETED) {
            throw new InvalidOrderStatusException("구매 확정된 상품은 취소할 수 없습니다.");
        }
        item.setStatus(OrderItemStatus.CANCELLED);
    }
    public void returnOrderItem(Long itemId) {
        OrderItem item = getOrderItem(itemId);
        if (item.getStatus() == OrderItemStatus.COMPLETED ||
                item.getStatus() == OrderItemStatus.CANCELLED) {
            throw new InvalidOrderStatusException("취소되었거나 구매 확정된 상품은 반품할 수 없습니다.");
        }
        item.setStatus(OrderItemStatus.RETURN_REQUESTED);
    }
    public void exchangeOrderItem(Long itemId) {
        OrderItem item = getOrderItem(itemId);
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