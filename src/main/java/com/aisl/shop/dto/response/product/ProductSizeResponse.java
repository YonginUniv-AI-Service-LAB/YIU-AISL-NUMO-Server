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
public class ProductSizeResponse {

    private Long id;
    private String size;
    private LocalDateTime createdAt;
}
