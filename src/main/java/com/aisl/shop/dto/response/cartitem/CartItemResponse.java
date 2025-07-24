package com.aisl.shop.dto.response.cartitem;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CartItemResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private String brandName;
    private String thumbnailUrl;

    private Integer quantity;
    private Integer unitprice;         // 단가 (옵션 기준 가격)
    private Integer totalPrice;    // 단가 * 수량
    private String color;
    private String size;
    private LocalDateTime createdAt;
}
