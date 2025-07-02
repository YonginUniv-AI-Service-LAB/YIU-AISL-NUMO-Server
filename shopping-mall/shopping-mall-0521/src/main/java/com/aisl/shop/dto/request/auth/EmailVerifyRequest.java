package com.aisl.shop.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVerifyRequest {

    @Schema(description = "인증할 이메일", example = "test@example.com")
    private String email;

    @Schema(description = "이메일로 받은 인증 코드", example = "123456")
    private String code;
}