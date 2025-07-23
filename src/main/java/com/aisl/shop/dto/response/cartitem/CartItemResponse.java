package com.aisl.shop.dto.response.cartitem;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CartItemResponse {
    private Long id;              // 장바구니 항목 ID
    private Long userId;          // 사용자 ID
    private Long productId;       // 상품 ID
    private String productName;   // 상품 이름
    private String brandName;     // 브랜드 이름
    private String thumbnailUrl;  // 썸네일 이미지 URL
    private Integer price;        // 상품 단가 (옵션 기준 단가)
    private Integer quantity;     // 수량
    private String color;         // 선택한 색상
    private String size;          // 선택한 사이즈
    private LocalDateTime createdAt;
}
