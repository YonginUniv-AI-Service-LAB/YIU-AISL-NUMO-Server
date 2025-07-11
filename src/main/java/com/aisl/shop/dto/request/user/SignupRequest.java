package com.aisl.shop.dto.request.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String email;
    private String password;
    private String name;
    private String nickname;
}
