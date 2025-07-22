package com.aisl.shop.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionResponse {

    private Long id;                     // 옵션 ID
    private Long productId;              // 상품 ID (연결용)
    private String color;                // 색상
    private List<ProductSizeResponse> sizes; // 사이즈 리스트
    private LocalDateTime createdAt;     // 생성일
}
