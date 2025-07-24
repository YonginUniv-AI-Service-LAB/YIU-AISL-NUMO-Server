package com.aisl.shop.exception.product;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(Long productId) {
        super("상품 ID [" + productId + "]에 해당하는 상품을 찾을 수 없습니다.");
    }
}
