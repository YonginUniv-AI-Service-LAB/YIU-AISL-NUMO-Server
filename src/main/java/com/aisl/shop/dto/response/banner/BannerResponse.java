package com.aisl.shop.dto.response.banner;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BannerResponse {
    private Long id;
    private String imageUrl;
    private LocalDateTime createdAt;
}
