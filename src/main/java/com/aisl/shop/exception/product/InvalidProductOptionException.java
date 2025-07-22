package com.aisl.shop.exception.product;

public class InvalidProductOptionException extends RuntimeException {
    public InvalidProductOptionException() {
        super("유효한 옵션이 하나 이상 존재해야 합니다.");
    }
}
