package com.aisl.shop.exception.order;

public class OrderItemNotFoundException extends RuntimeException {
    public OrderItemNotFoundException(String message) {
        super(message);
    }

    public OrderItemNotFoundException(Long itemId) {
        super("주문 항목 ID [" + itemId + "]에 해당하는 항목을 찾을 수 없습니다.");
    }
}
