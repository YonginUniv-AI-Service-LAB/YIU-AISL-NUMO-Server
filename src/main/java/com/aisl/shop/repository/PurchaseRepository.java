package com.aisl.shop.repository;

import com.aisl.shop.entity.Purchase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    // 월 전체 소비 합계
    @Query("SELECT SUM(p.amount) FROM Purchase p WHERE p.userId = :userId AND p.yearMonth = :yearMonth")
    Integer sumAmountByUserAndMonth(@Param("userId") Long userId, @Param("yearMonth") String yearMonth);

    // 월 카테고리별 소비 합계
    @Query("SELECT p.category, SUM(p.amount) FROM Purchase p WHERE p.userId = :userId AND p.yearMonth = :yearMonth GROUP BY p.category")
    List<Object[]> sumAmountByCategory(@Param("userId") Long userId, @Param("yearMonth") String yearMonth);
}
