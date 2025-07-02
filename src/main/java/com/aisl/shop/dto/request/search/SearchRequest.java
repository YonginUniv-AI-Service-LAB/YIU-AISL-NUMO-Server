package com.aisl.shop.dto.request.search;

import lombok.Getter;

@Getter
public class SearchRequest {
    private Long userId;
    private String keyword;
}
