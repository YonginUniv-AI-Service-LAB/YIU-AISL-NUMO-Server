package com.aisl.shop.service.order;

import com.aisl.shop.dto.request.order.OrderCreateRequest;
import com.aisl.shop.dto.request.order.OrderPayRequest;
import com.aisl.shop.dto.response.order.OrderDetailResponse;
import com.aisl.shop.dto.response.order.OrderListItemResponse;
import com.aisl.shop.entity.Order;
import com.aisl.shop.entity.Order.OrderStatus;
import com.aisl.shop.entity.Order.PaymentMethod;
import com.aisl.shop.entity.OrderItem;
import com.aisl.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public Long createOrder(OrderCreateRequest request) {
        int totalPrice = request.getItems().stream()
                .mapToInt(item -> item.getQuantity() * 34110)
                .sum();

        Order order = Order.builder()
                .userId(null)
                .email(request.getEmail())
                .name(request.getName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .paymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()))
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

    public OrderDetailResponse getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

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
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("결제 전 주문만 취소할 수 있습니다.");
        }

        order.setStatus(OrderStatus.CANCELLED);
    }

    public void payOrder(Long orderId, OrderPayRequest request) {
        if (!request.isMock()) {
            throw new IllegalArgumentException("실제 결제는 지원하지 않습니다.");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("이미 결제되었거나 취소된 주문입니다.");
        }

        order.setStatus(OrderStatus.PAID);
    }
}
