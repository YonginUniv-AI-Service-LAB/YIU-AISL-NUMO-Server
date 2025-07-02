package com.aisl.shop.dto.request.diary;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDiaryRequest {
    private String title;
    private String content;
}
