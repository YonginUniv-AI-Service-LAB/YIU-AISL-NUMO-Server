package com.aisl.shop.dto.response.habit;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PurchaseResponse {
    private Long id;
    private Integer amount;
    private String category;
    private String description;     // 🔹 설명 필드 포함
    private String yearMonth;
    private LocalDate purchaseDate; // 🔹 소비 날짜 포함
}
