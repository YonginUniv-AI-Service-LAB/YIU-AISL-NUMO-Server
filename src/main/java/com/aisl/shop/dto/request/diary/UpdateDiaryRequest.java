package com.aisl.shop.dto.request.diary;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDiaryRequest {

    @Size(max = 100, message = "제목은 100자 이내여야 합니다.")
    private String title;

    @Size(max = 1000, message = "내용은 1000자 이내여야 합니다.")
    private String content;
}
