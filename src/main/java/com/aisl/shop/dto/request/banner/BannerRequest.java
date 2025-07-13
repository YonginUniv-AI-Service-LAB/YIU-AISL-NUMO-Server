package com.aisl.shop.dto.request.banner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BannerRequest {
    private String title;
    private String imageUrl;
    private String linkUrl;
}
