package com.aisl.shop.dto.response.habit;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SpendingGoalResponse {
    private String yearMonth;
    private Integer targetAmount;
    private Integer currentSpending;
    private Double achievementRate;  // %
}
