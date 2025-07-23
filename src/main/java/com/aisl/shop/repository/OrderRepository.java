package com.aisl.shop.repository;

import com.aisl.shop.entity.Order;
import com.aisl.shop.entity.Order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 🔹 기본 전체 주문 조회
    List<Order> findByUserId(Long userId);

    // 🔹 필터 조건 기반 주문 조회 (상태, 시작일, 종료일)
    @Query("SELECT o FROM Order o WHERE o.userId = :userId " +
            "AND (:status IS NULL OR o.status = :status) " +
            "AND (:startDate IS NULL OR o.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR o.createdAt <= :endDate)")
    List<Order> findByFilters(
            @Param("userId") Long userId,
            @Param("status") OrderStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}
