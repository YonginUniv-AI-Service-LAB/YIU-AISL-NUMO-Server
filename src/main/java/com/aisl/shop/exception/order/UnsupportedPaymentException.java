package com.aisl.shop.exception.order;

public class UnsupportedPaymentException extends RuntimeException {
    public UnsupportedPaymentException(String message) {
        super(message);
    }
}
