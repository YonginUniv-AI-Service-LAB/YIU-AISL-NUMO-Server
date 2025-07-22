package com.aisl.shop.exception.review;

public class ReviewAccessDeniedException extends RuntimeException {
    public ReviewAccessDeniedException(String message) {
        super(message);
    }
}
