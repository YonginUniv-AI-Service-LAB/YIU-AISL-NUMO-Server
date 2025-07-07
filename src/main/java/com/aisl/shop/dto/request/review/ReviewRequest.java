package com.aisl.shop.dto.request.review;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {
    private Long productId;
    private int rating;
    private String sizeOpinion;
    private String colorOpinion;
    private String qualityOpinion;
    private String content;
    private List<String> imageUrls;
}