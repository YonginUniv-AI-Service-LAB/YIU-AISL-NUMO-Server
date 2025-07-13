package com.aisl.shop.dto.request.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginRequest {
    private String idToken;
}
