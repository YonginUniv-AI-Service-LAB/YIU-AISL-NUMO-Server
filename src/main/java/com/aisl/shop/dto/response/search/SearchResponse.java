package com.aisl.shop.dto.response.search;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchResponse {
    private Long id;
    private String keyword;
}
