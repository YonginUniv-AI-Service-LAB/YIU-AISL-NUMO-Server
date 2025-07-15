package com.aisl.shop.exception.wishlist;

public class WishlistAlreadyExistsException extends RuntimeException {
    public WishlistAlreadyExistsException(String message) {
        super(message);
    }
}
