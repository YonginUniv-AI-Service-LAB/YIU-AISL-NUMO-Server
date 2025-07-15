package com.aisl.shop.dto.request.habit;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SpendingGoalRequest {

    @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])$", message = "년도-월 형식이어야 합니다. 예: 2025-07")
    private String yearMonth;

    @NotNull(message = "목표 금액은 필수입니다.")
    @Min(value = 0, message = "목표 금액은 0 이상이어야 합니다.")
    private Integer targetAmount;
}
