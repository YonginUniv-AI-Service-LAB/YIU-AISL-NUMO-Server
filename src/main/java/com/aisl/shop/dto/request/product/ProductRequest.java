package com.aisl.shop.dto.request.product; // 관리자용

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private Integer price;
    private String description;
    private Long categoryId;
    private String thumbnailUrl;

    private String brand;
    private Integer discountRate;
    private Integer discountPrice;

    private List<String> keywords;
    private List<String> imageUrls;

    private List<ProductOptionRequest> options;
}
