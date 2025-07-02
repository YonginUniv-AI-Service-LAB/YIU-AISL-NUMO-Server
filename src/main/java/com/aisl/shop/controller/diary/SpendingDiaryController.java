package com.aisl.shop.controller.diary;

import com.aisl.shop.dto.request.diary.CreateDiaryRequest;
import com.aisl.shop.dto.request.diary.UpdateDiaryRequest;
import com.aisl.shop.dto.response.diary.DiaryResponse;
import com.aisl.shop.service.diary.SpendingDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
public class SpendingDiaryController {

    private final SpendingDiaryService diaryService;

    // 소비일기 작성
    @PostMapping
    public ResponseEntity<DiaryResponse> createDiary(@RequestParam Long userId,
                                                     @RequestBody CreateDiaryRequest request) {
        DiaryResponse response = diaryService.createDiary(userId, request);
        return ResponseEntity.ok(response);
    }

    // 소비일기 단건 조회 (날짜 기반)
    @GetMapping("/date")
    public ResponseEntity<DiaryResponse> getDiary(@RequestParam Long userId,
                                                  @RequestParam String date) {
        DiaryResponse response = diaryService.getDiary(userId, LocalDate.parse(date));
        return ResponseEntity.ok(response);
    }

    // 소비일기 월별 조회
    @GetMapping
    public ResponseEntity<List<DiaryResponse>> getMonthlyDiaries(@RequestParam Long userId,
                                                                 @RequestParam int year,
                                                                 @RequestParam int month) {
        List<DiaryResponse> responses = diaryService.getMonthlyDiaries(userId, year, month);
        return ResponseEntity.ok(responses);
    }

    // 소비일기 수정
    @PatchMapping("/{id}")
    public ResponseEntity<DiaryResponse> updateDiary(@RequestParam Long userId,
                                                     @PathVariable Long id,
                                                     @RequestBody UpdateDiaryRequest request) {
        DiaryResponse response = diaryService.updateDiary(userId, id, request);
        return ResponseEntity.ok(response);
    }

    // 소비일기 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiary(@RequestParam Long userId,
                                            @PathVariable Long id) {
        diaryService.deleteDiary(userId, id);
        return ResponseEntity.noContent().build();
    }
}
