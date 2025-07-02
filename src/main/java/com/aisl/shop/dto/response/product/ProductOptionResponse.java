package com.aisl.shop.dto.response.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProductOptionResponse {
    private Long id;
    private Long productId;
    private String color;
    private String size;
    private Integer stock;
    private LocalDateTime createdAt;
}
