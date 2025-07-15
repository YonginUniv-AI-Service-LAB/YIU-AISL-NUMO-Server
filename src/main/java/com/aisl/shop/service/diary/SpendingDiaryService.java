package com.aisl.shop.service.diary;

import com.aisl.shop.dto.request.diary.CreateDiaryRequest;
import com.aisl.shop.dto.request.diary.UpdateDiaryRequest;
import com.aisl.shop.dto.response.diary.DiaryResponse;
import com.aisl.shop.entity.SpendingDiary;
import com.aisl.shop.exception.diary.DiaryNotFoundException;
import com.aisl.shop.repository.SpendingDiaryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpendingDiaryService {

    private final SpendingDiaryRepository spendingDiaryRepository;

    @Transactional
    public DiaryResponse createDiary(Long userId, CreateDiaryRequest request) {
        SpendingDiary diary = SpendingDiary.builder()
                .userId(userId)
                .diaryDate(request.getDate())
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        spendingDiaryRepository.save(diary);
        return toResponse(diary);
    }

    @Transactional
    public DiaryResponse updateDiary(Long userId, Long diaryId, UpdateDiaryRequest request) {
        SpendingDiary diary = spendingDiaryRepository.findById(diaryId)
                .filter(d -> d.getUserId().equals(userId))
                .orElseThrow(() -> new DiaryNotFoundException("해당 소비일기를 찾을 수 없습니다."));

        diary.setTitle(request.getTitle());
        diary.setContent(request.getContent());

        return toResponse(diary);
    }

    public DiaryResponse getDiary(Long userId, LocalDate date) {
        SpendingDiary diary = spendingDiaryRepository.findByUserIdAndDiaryDate(userId, date)
                .orElseThrow(() -> new DiaryNotFoundException("해당 날짜의 소비일기가 없습니다."));
        return toResponse(diary);
    }

    public List<DiaryResponse> getMonthlyDiaries(Long userId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return spendingDiaryRepository.findAllByUserIdAndDiaryDateBetweenOrderByDiaryDateDesc(userId, start, end)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteDiary(Long userId, Long diaryId) {
        SpendingDiary diary = spendingDiaryRepository.findById(diaryId)
                .filter(d -> d.getUserId().equals(userId))
                .orElseThrow(() -> new DiaryNotFoundException("삭제할 소비일기가 없습니다."));
        spendingDiaryRepository.delete(diary);
    }

    private DiaryResponse toResponse(SpendingDiary diary) {
        return DiaryResponse.builder()
                .id(diary.getId())
                .date(diary.getDiaryDate())
                .title(diary.getTitle())
                .content(diary.getContent())
                .createdAt(diary.getCreatedAt())
                .updatedAt(diary.getUpdatedAt())
                .build();
    }
}
