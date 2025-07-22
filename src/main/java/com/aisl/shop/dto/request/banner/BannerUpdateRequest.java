package com.aisl.shop.dto.request.banner;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BannerUpdateRequest {

    @NotBlank(message = "이미지 URL은 필수입니다.")
    private String imageUrl;
}
