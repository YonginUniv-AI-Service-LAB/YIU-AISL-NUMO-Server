package com.aisl.shop.dto.response.habit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SpendingGoalResponse {
    private String yearMonth;
    private Integer targetAmount;
    private Integer currentSpending;
    private Double achievementRate;           // (%) 예: 84.3
    private Integer differenceFromGoal;       // 예: -22000

    private Integer lastMonthSpending;        // 예: 250000
    private Integer differenceFromLastMonth;  // 예: -22000

    private List<CategorySpending> categorySpendingList;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CategorySpending {
        private String category;      // 예: "상의"
        private Integer amount;       // 해당 카테고리 소비액
        private Double percentage;    // 전체 대비 비율 (%)
    }
}
