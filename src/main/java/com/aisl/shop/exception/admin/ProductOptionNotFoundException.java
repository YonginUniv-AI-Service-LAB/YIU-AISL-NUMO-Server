package com.aisl.shop.exception.admin;

public class ProductOptionNotFoundException extends RuntimeException {
    public ProductOptionNotFoundException(Long id) {
        super("해당 상품 옵션을 찾을 수 없습니다. (id: " + id + ")");
    }
}
