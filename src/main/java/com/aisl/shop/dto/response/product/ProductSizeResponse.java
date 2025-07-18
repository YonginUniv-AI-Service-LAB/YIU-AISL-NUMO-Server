package com.aisl.shop.dto.response.product;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProductSizeResponse {
    private Long id;
    private String size;
    private LocalDateTime createdAt;
}
