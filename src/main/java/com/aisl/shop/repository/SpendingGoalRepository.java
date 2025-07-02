package com.aisl.shop.repository;

import com.aisl.shop.entity.SpendingGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpendingGoalRepository extends JpaRepository<SpendingGoal, Long> {
    Optional<SpendingGoal> findByUserIdAndYearMonth(Long userId, String yearMonth);
}
