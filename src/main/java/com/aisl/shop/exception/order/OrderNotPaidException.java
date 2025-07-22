package com.aisl.shop.exception.order;

public class OrderNotPaidException extends RuntimeException {
    public OrderNotPaidException(String message) {
        super(message);
    }
}
