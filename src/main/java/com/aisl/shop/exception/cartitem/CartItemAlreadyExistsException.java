package com.aisl.shop.exception.cartitem;

public class CartItemAlreadyExistsException extends RuntimeException {
    public CartItemAlreadyExistsException(Long productId, String color, String size) {
        super("이미 장바구니에 담긴 상품입니다. [상품ID: " + productId + ", 색상: " + color + ", 사이즈: " + size + "]");
    }
}
