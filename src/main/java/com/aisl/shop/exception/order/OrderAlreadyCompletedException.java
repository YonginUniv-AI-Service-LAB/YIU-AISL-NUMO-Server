package com.aisl.shop.exception.order;

public class OrderAlreadyCompletedException extends RuntimeException {
    public OrderAlreadyCompletedException(String message) {
        super(message);
    }
}
