package com.aisl.shop.exception.product;

public class ProductOptionNotFoundException extends RuntimeException {
    public ProductOptionNotFoundException(String message) {
        super(message);
    }

    public ProductOptionNotFoundException(Long optionId) {
        super("옵션 ID [" + optionId + "]에 해당하는 옵션을 찾을 수 없습니다.");
    }
}
