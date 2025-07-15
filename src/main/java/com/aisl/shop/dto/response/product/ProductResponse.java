package com.aisl.shop.dto.response.product;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private Integer price;
    private String description;
    private Long categoryId;
    private String thumbnailUrl;
    private LocalDateTime createdAt;

    private String brand;
    private Integer discountRate;
    private Integer discountPrice;

    private List<String> keywords;
    private List<String> imageUrls;

    private List<ProductOptionResponse> options;
}
