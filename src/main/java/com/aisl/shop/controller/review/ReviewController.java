package com.aisl.shop.controller.review;

import com.aisl.shop.dto.request.review.ReviewRequest;
import com.aisl.shop.dto.response.review.ReviewResponse;
import com.aisl.shop.service.review.ReviewService;
import com.aisl.shop.config.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewRequest request,
                                          @AuthenticationPrincipal CustomUserDetails user) {
        reviewService.createReview(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "리뷰가 성공적으로 등록되었습니다."));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId,
                                          @AuthenticationPrincipal CustomUserDetails user) {
        reviewService.deleteReview(reviewId, user.getId(), user.isAdmin());
        return ResponseEntity.ok(Map.of("message", "리뷰가 삭제되었습니다."));
    }
}
