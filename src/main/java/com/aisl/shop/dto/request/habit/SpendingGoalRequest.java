package com.aisl.shop.dto.request.habit;

import lombok.Getter;

@Getter
public class SpendingGoalRequest {
    private String yearMonth;
    private Integer targetAmount;
}
