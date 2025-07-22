package com.aisl.shop.exception.productoption;

public class InvalidStockQuantityException extends RuntimeException {
    public InvalidStockQuantityException(int stock) {
        super("유효하지 않은 재고 수량입니다. 재고는 0 이상이어야 하며 현재 값: " + stock);
    }
}
