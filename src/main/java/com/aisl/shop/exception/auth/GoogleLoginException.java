package com.aisl.shop.exception.auth;

public class GoogleLoginException extends RuntimeException {
    public GoogleLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
