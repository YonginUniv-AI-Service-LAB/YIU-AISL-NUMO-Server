package com.aisl.shop.dto.response.category;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}
