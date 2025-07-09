package com.aisl.shop.dto.request.product;

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

    //  옵션 정보 포함
    private List<ProductOptionRequest> options;
}
