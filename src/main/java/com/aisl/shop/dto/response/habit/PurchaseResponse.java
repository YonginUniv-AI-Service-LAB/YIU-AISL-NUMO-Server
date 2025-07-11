package com.aisl.shop.dto.response.habit;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PurchaseResponse {
    private Long id;
    private Long userId;
    private Integer amount;
    private String category;
    private String yearMonth;
}
