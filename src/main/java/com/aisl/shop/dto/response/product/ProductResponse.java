package com.aisl.shop.dto.response.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private Long categoryId;
    private String thumbnailUrl;
    private LocalDateTime createdAt;

    //  옵션 정보 포함
    private List<ProductOptionResponse> options;
}
