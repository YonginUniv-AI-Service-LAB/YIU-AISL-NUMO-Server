package com.aisl.shop.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVerifyRequest {

    @Schema(description = "인증할 이메일", example = "test@example.com")
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    @Schema(description = "이메일로 받은 인증 코드", example = "123456")
    @NotBlank(message = "인증 코드는 필수입니다.")
    private String code;
}
