package com.aisl.shop.controller.habit;

import com.aisl.shop.dto.request.habit.SpendingGoalRequest;
import com.aisl.shop.dto.response.habit.SpendingGoalResponse;
import com.aisl.shop.service.habit.SpendingGoalService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/habits/spending-goals")
@RequiredArgsConstructor
public class SpendingGoalController {

    private final SpendingGoalService spendingHabitService;

    // 🔹 소비 목표 생성 또는 수정
    @PostMapping
    public ResponseEntity<SpendingGoalResponse> upsertSpendingGoal(
            @RequestHeader("X-USER-ID") Long userId,
            @Valid @RequestBody SpendingGoalRequest request
    ) {
        SpendingGoalResponse response = spendingHabitService.upsertSpendingGoal(userId, request);
        return ResponseEntity.ok(response);
    }

    // 🔹 소비 목표 조회
    @GetMapping("/{yearMonth}")
    public ResponseEntity<SpendingGoalResponse> getSpendingGoal(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable
            @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])$", message = "yearMonth는 YYYY-MM 형식이어야 합니다.")
            String yearMonth
    ) {
        SpendingGoalResponse response = spendingHabitService.getSpendingGoal(userId, yearMonth);
        return ResponseEntity.ok(response);
    }

    // 🔹 소비 목표 삭제
    @DeleteMapping("/{yearMonth}")
    public ResponseEntity<Void> deleteSpendingGoal(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable
            @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])$", message = "yearMonth는 YYYY-MM 형식이어야 합니다.")
            String yearMonth
    ) {
        spendingHabitService.deleteSpendingGoal(userId, yearMonth);
        return ResponseEntity.noContent().build();
    }
}
