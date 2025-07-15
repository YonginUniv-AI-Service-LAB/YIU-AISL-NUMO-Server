package com.aisl.shop.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionRequest {

    @NotBlank(message = "색상을 입력해주세요.")
    private String color;

    @NotBlank(message = "사이즈를 입력해주세요.")
    private String size;

    @NotNull(message = "재고 수량을 입력해주세요.")
    @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다.")
    private Integer stock;

    @NotNull(message = "추가 금액을 입력해주세요.")
    @Min(value = 0, message = "추가 금액은 0 이상이어야 합니다.")
    private Integer additionalPrice;
}
