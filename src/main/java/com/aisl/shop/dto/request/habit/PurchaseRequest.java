package com.aisl.shop.dto.request.habit;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PurchaseRequest {

    @NotNull(message = "회원 ID는 필수입니다.")
    private Long userId;

    @NotNull(message = "금액은 필수입니다.")
    @Min(value = 1, message = "금액은 1 이상이어야 합니다.")
    private Integer amount;

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;

    @Pattern(
            regexp = "^[0-9]{4}-(0[1-9]|1[0-2])$",
            message = "날짜는 yyyy-MM 형식이어야 합니다."
    )
    private String yearMonth;
}
