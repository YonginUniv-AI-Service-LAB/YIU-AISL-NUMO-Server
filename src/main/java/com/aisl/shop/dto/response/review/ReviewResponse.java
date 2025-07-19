package com.aisl.shop.dto.response.review;

import com.aisl.shop.enums.review.ColorOpinion;
import com.aisl.shop.enums.review.QualityOpinion;
import com.aisl.shop.enums.review.SizeOpinion;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Long reviewId;
    private Long userId;
    private int rating;

    private SizeOpinion sizeOpinion;
    private ColorOpinion colorOpinion;
    private QualityOpinion qualityOpinion;

    private String content;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
}
