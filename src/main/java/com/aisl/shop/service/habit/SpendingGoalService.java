package com.aisl.shop.service.habit;

import com.aisl.shop.dto.request.habit.SpendingGoalRequest;
import com.aisl.shop.dto.response.habit.SpendingGoalResponse;
import com.aisl.shop.entity.SpendingGoal;
import com.aisl.shop.repository.SpendingGoalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpendingGoalService {

    private final SpendingGoalRepository spendingGoalRepository;

    /**
     * 소비 목표 생성 또는 수정 (Upsert)
     */
    @Transactional
    public SpendingGoalResponse upsertSpendingGoal(Long userId, SpendingGoalRequest request) {
        Optional<SpendingGoal> optionalGoal = spendingGoalRepository.findByUserIdAndYearMonth(
                userId,
                request.getYearMonth()
        );

        SpendingGoal goal = optionalGoal.orElseGet(() ->
                SpendingGoal.builder()
                        .userId(userId)
                        .yearMonth(request.getYearMonth())
                        .targetAmount(0)
                        .currentSpending(0)
                        .build()
        );

        goal.setTargetAmount(request.getTargetAmount());
        spendingGoalRepository.save(goal);

        return toResponse(goal);
    }

    /**
     * 소비 목표 조회
     */
    public SpendingGoalResponse getSpendingGoal(Long userId, String yearMonth) {
        SpendingGoal goal = spendingGoalRepository.findByUserIdAndYearMonth(userId, yearMonth)
                .orElseThrow(() -> new IllegalArgumentException("해당 월의 소비 목표가 존재하지 않습니다."));

        return toResponse(goal);
    }

    /**
     * 소비 목표 삭제
     */
    @Transactional
    public void deleteSpendingGoal(Long userId, String yearMonth) {
        SpendingGoal goal = spendingGoalRepository.findByUserIdAndYearMonth(userId, yearMonth)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 소비 목표가 존재하지 않습니다."));
        spendingGoalRepository.delete(goal);
    }

    /**
     * 엔티티 → 응답 DTO 변환
     */
    private SpendingGoalResponse toResponse(SpendingGoal goal) {
        return new SpendingGoalResponse(
                goal.getYearMonth(),
                goal.getTargetAmount(),
                goal.getCurrentSpending(),
                goal.calculateAchievementRate()
        );
    }
}
