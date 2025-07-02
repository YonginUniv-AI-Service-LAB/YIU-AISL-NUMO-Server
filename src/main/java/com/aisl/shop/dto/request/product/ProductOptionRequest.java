package com.aisl.shop.dto.request.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOptionRequest {
    private String color;
    private String size;
    private Integer stock;
}
