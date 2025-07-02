package com.aisl.shop.dto.request.order;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateRequest {

    private String email;
    private String name;
    private String phone;
    private String address;
    private String paymentMethod; // "TOSS", "KAKAO" 등

    private List<OrderItemDto> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemDto {
        private Long productId;
        private Long optionId; // nullable
        private Integer quantity;
    }
}
