package com.aisl.shop.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionRequest {
    private String color;
    private String size;
    private Integer stock;
}
