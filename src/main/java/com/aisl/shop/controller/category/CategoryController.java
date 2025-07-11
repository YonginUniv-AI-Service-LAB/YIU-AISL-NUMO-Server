package com.aisl.shop.controller.category;

import com.aisl.shop.dto.request.category.CategoryRequest;
import com.aisl.shop.dto.response.category.CategoryResponse;
import com.aisl.shop.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "카테고리", description = "카테고리 관련 API")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "전체 카테고리 목록 조회")
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
