package com.aisl.shop.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken; // ✅ 추가
    private UserInfo user;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class UserInfo {
        private Long id;
        private String email;
        private String name;
    }
}
