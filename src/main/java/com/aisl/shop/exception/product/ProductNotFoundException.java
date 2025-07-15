package com.aisl.shop.exception.product;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("해당 상품을 찾을 수 없습니다. ID = " + id);
    }
}
