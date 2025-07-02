package com.aisl.shop.dto.request.auth;
//소셜 로그인 전용
import lombok.Getter;

@Getter
public class GoogleLoginRequest {
    private String idToken;
}
