package com.aisl.shop.service.habit;

import com.aisl.shop.dto.request.habit.SpendingGoalRequest;
import com.aisl.shop.dto.response.habit.SpendingGoalResponse;
import com.aisl.shop.entity.SpendingGoal;
import com.aisl.shop.repository.SpendingGoalRepository;
import com.aisl.shop.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpendingGoalService {

    private final SpendingGoalRepository spendingGoalRepository;
    private final PurchaseRepository purchaseRepository; // 추가

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

        return getSpendingGoal(userId, request.getYearMonth());
    }

    /**
     * 소비 목표 조회 및 통계 포함
     */
    public SpendingGoalResponse getSpendingGoal(Long userId, String yearMonth) {
        SpendingGoal goal = spendingGoalRepository.findByUserIdAndYearMonth(userId, yearMonth)
                .orElseThrow(() -> new IllegalArgumentException("해당 월의 소비 목표가 존재하지 않습니다."));

        // 현재 소비액
        Integer currentSpending = purchaseRepository.sumAmountByUserAndMonth(userId, yearMonth);
        if (currentSpending == null) currentSpending = 0;

        // 전월 소비액
        String lastMonth = getPreviousMonth(yearMonth);
        Integer lastMonthSpending = purchaseRepository.sumAmountByUserAndMonth(userId, lastMonth);
        if (lastMonthSpending == null) lastMonthSpending = 0;

        // 카테고리별 소비
        List<Object[]> categoryData = purchaseRepository.sumAmountByCategory(userId, yearMonth);
        List<SpendingGoalResponse.CategorySpending> categories = new ArrayList<>();

        for (Object[] row : categoryData) {
            String category = (String) row[0];
            Integer amount = ((BigDecimal) row[1]).intValue();
            double percentage = currentSpending > 0 ? (amount * 100.0 / currentSpending) : 0.0;

            categories.add(SpendingGoalResponse.CategorySpending.builder()
                    .category(category)
                    .amount(amount)
                    .percentage(percentage)
                    .build());
        }

        // 응답 DTO 생성
        return SpendingGoalResponse.builder()
                .yearMonth(yearMonth)
                .targetAmount(goal.getTargetAmount())
                .currentSpending(currentSpending)
                .achievementRate(goal.getTargetAmount() > 0 ?
                        (currentSpending * 100.0 / goal.getTargetAmount()) : 0.0)
                .differenceFromGoal(currentSpending - goal.getTargetAmount())
                .lastMonthSpending(lastMonthSpending)
                .differenceFromLastMonth(currentSpending - lastMonthSpending)
                .categorySpendingList(categories)
                .build();
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
     * yyyy-MM → 전월 yyyy-MM 계산
     */
    private String getPreviousMonth(String yearMonth) {
        LocalDate date = LocalDate.parse(yearMonth + "-01");
        return date.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }
}
