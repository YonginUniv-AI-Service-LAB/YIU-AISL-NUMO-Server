package com.aisl.shop.dto.request.cartitem;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.*;

@Getter
@Setter
public class CartItemRequest {

    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    @NotBlank(message = "브랜드명을 입력해주세요.")
    private String brandName;

    @NotBlank(message = "상품명을 입력해주세요.")
    private String productName;

    @NotBlank(message = "색상을 선택해주세요.")
    private String color;

    @NotBlank(message = "사이즈를 선택해주세요.")
    private String size;

    @NotNull(message = "수량을 입력해주세요.")
    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    private Integer quantity;
}
