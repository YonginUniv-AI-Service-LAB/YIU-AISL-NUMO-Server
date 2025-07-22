package com.aisl.shop.exception.product;

public class InvalidDiscountRateException extends RuntimeException {
    public InvalidDiscountRateException(int rate) {
        super("할인율은 0 ~ 100 사이여야 합니다. 현재 값: " + rate);
    }
}
