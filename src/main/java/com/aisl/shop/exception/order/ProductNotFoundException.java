package com.aisl.shop.exception.order;

public class ProductNotFoundException extends RuntimeException {

    // ✅ 이 생성자가 있어야 문자열 메시지를 넣을 수 있어
    public ProductNotFoundException(String message) {
        super(message);
    }
}
