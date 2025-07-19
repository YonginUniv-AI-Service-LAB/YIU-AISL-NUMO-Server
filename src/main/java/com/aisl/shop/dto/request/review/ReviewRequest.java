package com.aisl.shop.dto.request.review;

import com.aisl.shop.enums.review.ColorOpinion;
import com.aisl.shop.enums.review.QualityOpinion;
import com.aisl.shop.enums.review.SizeOpinion;
import lombok.*;

import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {

    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    @Min(value = 1, message = "별점은 최소 1점 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 최대 5점까지 가능합니다.")
    private int rating;

    @NotNull(message = "사이즈 선택은 필수입니다.")
    private SizeOpinion sizeOpinion;

    @NotNull(message = "색감 선택은 필수입니다.")
    private ColorOpinion colorOpinion;

    @NotNull(message = "퀄리티 선택은 필수입니다.")
    private QualityOpinion qualityOpinion;

    @Size(min = 20, message = "상세 후기는 최소 20자 이상 작성해야 합니다.")
    private String content;

    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();  // 기본값 처리
}
