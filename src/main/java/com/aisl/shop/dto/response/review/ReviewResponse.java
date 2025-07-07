package com.aisl.shop.dto.response.review;

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
    private String sizeOpinion;
    private String colorOpinion;
    private String qualityOpinion;
    private String content;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
}