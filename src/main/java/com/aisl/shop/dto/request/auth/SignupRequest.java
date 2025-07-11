package com.aisl.shop.dto.request.auth;

import lombok.Getter;

@Getter
public class SignupRequest {
    private String email;
    private String password;
    private String name;
    private String nickname;
}
