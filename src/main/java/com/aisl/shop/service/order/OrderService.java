package com.aisl.shop.service.order;

import com.aisl.shop.dto.request.order.OrderCreateRequest;
import com.aisl.shop.dto.request.order.OrderPayRequest;
import com.aisl.shop.dto.response.order.OrderDetailResponse;
import com.aisl.shop.dto.response.order.OrderListItemResponse;
import com.aisl.shop.entity.Order;
import com.aisl.shop.entity.Order.OrderStatus;
import com.aisl.shop.entity.Order.PaymentMethod;
import com.aisl.shop.entity.OrderItem;
import com.aisl.shop.enums.OrderItemStatus;
import com.aisl.shop.exception.order.*;
import com.aisl.shop.repository.OrderRepository;
import com.aisl.shop.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    // 🔹 주문 생성
    public Long createOrder(OrderCreateRequest request) {
        int totalPrice = request.getItems().stream()
                .mapToInt(item -> item.getQuantity() * 34110)
                .sum();

        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.valueOf(request.getPaymentMethod());
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderStatusException("유효하지 않은 결제 방식입니다.");
        }

        Order order = Order.builder()
                .userId(null)
                .email(request.getEmail())
                .name(request.getName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .paymentMethod(paymentMethod)
                .totalPrice(totalPrice)
                .status(OrderStatus.PENDING)
                .build();

        List<OrderItem> items = request.getItems().stream()
                .map(dto -> OrderItem.builder()
                        .order(order)
                        .productId(dto.getProductId())
                        .optionId(dto.getOptionId())
                        .quantity(dto.getQuantity())
                        .unitPrice(34110)
                        .totalPrice(dto.getQuantity() * 34110)
                        .status(OrderItemStatus.PAID)
                        .build()
                ).collect(Collectors.toList());

        order.setItems(items);
        orderRepository.save(order);
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

    // 🔧 수정된 부분
    public List<OrderListItemResponse> getFilteredOrderList(Long userId, String status, LocalDate startDate, LocalDate endDate) {
        OrderStatus enumStatus = null;
        if (status != null) {
            try {
                enumStatus = OrderStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidOrderStatusException("유효하지 않은 주문 상태입니다.");
            }
        }

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.plusDays(1).atStartOfDay().minusNanos(1) : null;

        List<Order> orders = orderRepository.findByFilters(userId, enumStatus, startDateTime, endDateTime);
        return orders.stream().map(order -> OrderListItemResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .build()).collect(Collectors.toList());
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
                                .productName("상품명")
                                .optionName("옵션명")
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
        item.setStatus(OrderItemStatus.CANCELLED);
    }

    public void returnOrderItem(Long itemId) {
        OrderItem item = getOrderItem(itemId);
        item.setStatus(OrderItemStatus.RETURN_REQUESTED);
    }

    public void exchangeOrderItem(Long itemId) {
        OrderItem item = getOrderItem(itemId);
        item.setStatus(OrderItemStatus.EXCHANGE_REQUESTED);
    }

    private OrderItem getOrderItem(Long itemId) {
        return orderItemRepository.findById(itemId)
                .orElseThrow(() -> new OrderItemNotFoundException("주문 상품을 찾을 수 없습니다."));
    }
}
