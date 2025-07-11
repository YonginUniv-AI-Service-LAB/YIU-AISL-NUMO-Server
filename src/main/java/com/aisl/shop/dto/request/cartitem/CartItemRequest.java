package com.aisl.shop.dto.request.cartitem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {
    private Long productId;
    private String color;
    private String size;
    private Integer quantity;
}
