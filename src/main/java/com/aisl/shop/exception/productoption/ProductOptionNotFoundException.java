package com.aisl.shop.exception.productoption;

public class ProductOptionNotFoundException extends RuntimeException {
    public ProductOptionNotFoundException(Long id) {
        super("해당 옵션을 찾을 수 없습니다. ID = " + id);
    }
}
