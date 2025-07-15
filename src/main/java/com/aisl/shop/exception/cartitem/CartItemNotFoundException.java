package com.aisl.shop.exception.cartitem;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(Long cartItemId) {
        super("해당 장바구니 항목을 찾을 수 없습니다. ID = " + cartItemId);
    }
}
