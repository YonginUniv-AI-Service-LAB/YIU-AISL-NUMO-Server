package com.aisl.shop.exception.auth;

public class ForbiddenUserAccessException extends RuntimeException {
    public ForbiddenUserAccessException(String message) {
        super(message);
    }
}
