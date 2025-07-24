package com.aisl.shop.exception.order;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(Long orderId) {
        super("주문 ID [" + orderId + "]에 해당하는 주문을 찾을 수 없습니다.");
    }
}
