package com.aisl.shop.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAuthRequest {

    @Schema(description = "인증 코드를 받을 이메일", example = "test@example.com")
    private String email;
}