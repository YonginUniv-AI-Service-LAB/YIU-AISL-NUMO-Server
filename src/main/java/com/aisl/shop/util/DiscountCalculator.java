package com.aisl.shop.util;

public class DiscountCalculator {

    // 할인율 → 할인가 계산
    public static Integer calculateDiscountPrice(Integer price, Integer discountRate) {
        if (price == null || discountRate == null) return null;
        return price - (price * discountRate / 100);
    }

    // 할인가 → 할인율 계산
    public static Integer calculateDiscountRate(Integer price, Integer discountPrice) {
        if (price == null || discountPrice == null || price == 0) return null;
        return 100 - (discountPrice * 100 / price);
    }
}
