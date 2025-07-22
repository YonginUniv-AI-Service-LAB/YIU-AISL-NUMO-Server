package com.aisl.shop.dto.response.product;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ProductCreateResponse {

    // 기본 정보
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private String brand;
    private LocalDateTime createdAt;

    // 카테고리
    private Long categoryId;

    // 할인 정보
    private Integer discountRate;
    private Integer discountPrice;

    // 이미지/키워드
    private String thumbnailUrl;
    private List<String> imageUrls;
    private List<String> keywords;

    // 옵션 (색상 + 사이즈 리스트)
    private List<ProductOptionResponse> options;
}
