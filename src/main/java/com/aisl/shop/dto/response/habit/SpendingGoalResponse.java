package com.aisl.shop.dto.response.habit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
@AllArgsConstructor
public class SpendingGoalResponse {
    private String userName;                 // 🔹 사용자 이름 추가
    private String yearMonth;
    private Integer targetAmount;
    private Integer currentSpending;
    private Double achievementRate;
    private Integer differenceFromGoal;
    private Integer lastMonthSpending;
    private Integer differenceFromLastMonth;
    private List<CategorySpending> categorySpendingList;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CategorySpending {
        private String category;
        private Integer amount;
        private Double percentage;
    }
}
