package com.aisl.shop.dto.request.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private Integer price;
    private String description;
    private Long categoryId;
    private String thumbnailUrl;
}
