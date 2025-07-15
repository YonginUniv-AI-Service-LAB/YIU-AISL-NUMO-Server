package com.aisl.shop.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionResponse {
    private Long id;
    private Long productId;
    private String color;
    private String size;
    private Integer stock;
    private Integer additionalPrice;
    private LocalDateTime createdAt;
}
