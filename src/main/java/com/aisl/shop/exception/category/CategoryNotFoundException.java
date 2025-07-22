package com.aisl.shop.exception.category;

public class CategoryNotFoundException extends RuntimeException {

    // Long 타입을 받는 생성자 추가
    public CategoryNotFoundException(Long categoryId) {
        super("해당 카테고리를 찾을 수 없습니다. ID: " + categoryId);
    }

    // 기존 String 타입 생성자 유지해도 무방
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
