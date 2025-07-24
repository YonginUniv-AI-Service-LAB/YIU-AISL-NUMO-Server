package com.aisl.shop.dto.request.product;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductCreateRequest {

    @NotBlank(message = "상품명을 입력해주세요.")
    private String name;

    @NotNull(message = "가격을 입력해주세요.")
    @Positive(message = "가격은 0보다 커야 합니다.")
    private Integer price;

    @NotBlank(message = "상품 설명을 입력해주세요.")
    private String description;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Long categoryId;

    @NotBlank(message = "대표 이미지 URL을 입력해주세요.")
    private String thumbnailUrl;

    @NotBlank(message = "브랜드명을 입력해주세요.")
    private String brand;

    @Min(value = 0, message = "할인율은 0 이상이어야 합니다.")
    @Max(value = 100, message = "할인율은 100 이하이어야 합니다.")
    private Integer discountRate;

    @Min(value = 0, message = "할인 가격은 0 이상이어야 합니다.")
    private Integer discountPrice;

    @Size(min = 1, message = "색상을 하나 이상 입력해주세요.")
    private List<@NotBlank String> colors;

    @Size(min = 1, message = "사이즈를 하나 이상 입력해주세요.")
    private List<@NotBlank String> sizes;

    @Size(min = 1, message = "키워드를 하나 이상 입력해주세요.")
    private List<@NotBlank String> keywords;

    @Size(min = 1, message = "이미지를 하나 이상 등록해주세요.")
    private List<@NotBlank String> imageUrls;
}