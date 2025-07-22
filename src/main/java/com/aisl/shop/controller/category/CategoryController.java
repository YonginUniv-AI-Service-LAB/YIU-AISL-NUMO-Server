package com.aisl.shop.controller.category;

import com.aisl.shop.dto.request.category.CategoryRequest;
import com.aisl.shop.dto.response.category.CategoryResponse;
import com.aisl.shop.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "카테고리", description = "카테고리 관련 API")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 전체 카테고리 목록 조회
     */
    @GetMapping
    @Operation(summary = "전체 카테고리 목록 조회")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    /**
     * 카테고리 등록
     */
    @PostMapping
    @Operation(summary = "카테고리 등록")
    public ResponseEntity<Void> addCategory(
            @Valid @RequestBody CategoryRequest request
    ) {
        categoryService.addCategory(request);
        return ResponseEntity.ok().build(); // 200 OK
    }

    /**
     * 카테고리 수정
     */
    @PatchMapping("/{id}")
    @Operation(summary = "카테고리 이름 수정")
    public ResponseEntity<Void> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request
    ) {
        categoryService.updateCategory(id, request);
        return ResponseEntity.ok().build();
    }

    /**
     * 카테고리 삭제
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "카테고리 삭제")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
