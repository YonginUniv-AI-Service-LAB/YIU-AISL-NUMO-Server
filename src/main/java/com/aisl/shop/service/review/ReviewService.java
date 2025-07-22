package com.aisl.shop.service.review;

import com.aisl.shop.dto.request.review.ReviewRequest;
import com.aisl.shop.dto.response.review.ReviewResponse;
import com.aisl.shop.entity.Review;
import com.aisl.shop.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public void createReview(Long userId, ReviewRequest request) {
        // 이미지 리스트가 null이면 빈 리스트로 초기화
        List<String> imageUrls = request.getImageUrls() != null ? request.getImageUrls() : Collections.emptyList();

        Review review = Review.builder()
                .userId(userId)
                .productId(request.getProductId())
                .rating(request.getRating())
                .sizeOpinion(request.getSizeOpinion())
                .colorOpinion(request.getColorOpinion())
                .qualityOpinion(request.getQualityOpinion())
                .content(request.getContent())
                .imageUrls(imageUrls)
                .build();

        reviewRepository.save(review);
    }

    public List<ReviewResponse> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(review -> ReviewResponse.builder()
                        .reviewId(review.getId())
                        .userId(review.getUserId())
                        .rating(review.getRating())
                        .sizeOpinion(review.getSizeOpinion())
                        .colorOpinion(review.getColorOpinion())
                        .qualityOpinion(review.getQualityOpinion())
                        .content(review.getContent())
                        .imageUrls(review.getImageUrls())
                        .createdAt(review.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public void deleteReview(Long reviewId, Long userId, boolean isAdmin) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));

        if (!Objects.equals(review.getUserId(), userId) && !isAdmin) {
            throw new IllegalArgumentException("리뷰를 삭제할 권한이 없습니다.");
        }

        reviewRepository.delete(review);
    }
}
