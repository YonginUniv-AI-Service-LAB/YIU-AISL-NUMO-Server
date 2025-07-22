package com.aisl.shop.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 20, message = "이름은 최대 20자까지 입력 가능합니다.")
    private String name;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 20, message = "닉네임은 최대 20자까지 입력 가능합니다.")
    private String nickname;
}
