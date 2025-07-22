package com.aisl.shop.exception.order;

public class OrderAlreadyProcessedException extends RuntimeException {
    public OrderAlreadyProcessedException(String message) {
        super(message);
    }
}
