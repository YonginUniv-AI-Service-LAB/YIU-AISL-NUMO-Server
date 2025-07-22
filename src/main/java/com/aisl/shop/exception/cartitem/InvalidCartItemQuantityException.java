package com.aisl.shop.exception.cartitem;

public class InvalidCartItemQuantityException extends RuntimeException {
    public InvalidCartItemQuantityException(int quantity) {
        super("유효하지 않은 수량입니다. 수량은 최소 1 이상이어야 합니다. 현재 수량: " + quantity);
    }
}
