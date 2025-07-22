package com.aisl.shop.exception.productoption;

public class DuplicateProductOptionException extends RuntimeException {
    public DuplicateProductOptionException(String color, String size) {
        super("이미 존재하는 옵션입니다. 색상: " + color + ", 사이즈: " + size);
    }
}
