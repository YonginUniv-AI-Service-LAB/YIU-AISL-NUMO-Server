package com.aisl.shop.repository;

import com.aisl.shop.entity.SpendingDiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SpendingDiaryRepository extends JpaRepository<SpendingDiary, Long> {

    // 단일 날짜 조회
    Optional<SpendingDiary> findByUserIdAndDiaryDate(Long userId, LocalDate diaryDate);

    // 특정 월(시작 ~ 끝 날짜) 범위 조회
    List<SpendingDiary> findAllByUserIdAndDiaryDateBetweenOrderByDiaryDateDesc(
            Long userId, LocalDate startDate, LocalDate endDate
    );
}
