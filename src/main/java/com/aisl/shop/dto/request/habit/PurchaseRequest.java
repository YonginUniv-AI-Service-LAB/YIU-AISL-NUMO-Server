package com.aisl.shop.dto.request.habit;

import lombok.Getter;

@Getter
public class PurchaseRequest {
    private Long userId;
    private Integer amount;
    private String category;   // 예: "상의", "하의", ...
    private String yearMonth;  // 예: "2025-07"
}
