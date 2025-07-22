package com.aisl.shop.dto.request.search;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SearchRequest {

    @NotNull(message = "회원 ID는 필수입니다.")
    private Long userId;

    @NotBlank(message = "검색어는 필수입니다.")
    private String keyword;
}
