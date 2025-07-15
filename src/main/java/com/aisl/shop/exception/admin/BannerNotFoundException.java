package com.aisl.shop.exception.admin;

public class BannerNotFoundException extends RuntimeException {
    public BannerNotFoundException(Long id) {
        super("해당 배너를 찾을 수 없습니다. (id: " + id + ")");
    }
}
