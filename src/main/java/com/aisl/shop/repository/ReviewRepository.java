package com.aisl.shop.repository;

import com.aisl.shop.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * 상품별 리뷰 전체 조회
     */
    List<Review> findByProductId(Long productId);

    /**
     * 유저가 이미 해당 상품에 리뷰를 작성했는지 확인
     */
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
