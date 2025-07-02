package com.aisl.shop.dto.request.diary;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateDiaryRequest {
    private LocalDate date;
    private String title;
    private String content;
}
