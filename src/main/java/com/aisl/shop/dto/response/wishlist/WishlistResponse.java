package com.aisl.shop.dto.response.wishlist;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class WishlistResponse {
    private Long id;
    private Long productId;
    private LocalDateTime createdAt;
}
