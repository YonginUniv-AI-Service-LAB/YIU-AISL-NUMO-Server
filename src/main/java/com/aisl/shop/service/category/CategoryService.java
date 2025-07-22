package com.aisl.shop.service.category;

import com.aisl.shop.dto.request.category.CategoryRequest;
import com.aisl.shop.dto.response.category.CategoryResponse;
import com.aisl.shop.entity.Category;
import com.aisl.shop.exception.category.CategoryAlreadyExistsException;
import com.aisl.shop.exception.category.CategoryNotFoundException;
import com.aisl.shop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 전체 조회
     */
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리 등록
     */
    @Transactional
    public void addCategory(CategoryRequest request) {
        boolean exists = categoryRepository.existsByName(request.getName());
        if (exists) {
            throw new CategoryAlreadyExistsException("이미 존재하는 카테고리입니다.");
        }

        Category category = Category.builder()
                .name(request.getName())
                .createdAt(LocalDateTime.now())
                .build();

        categoryRepository.save(category);
    }

    /**
     * 카테고리 수정
     */
    @Transactional
    public void updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("해당 카테고리를 찾을 수 없습니다."));

        category.setName(request.getName());
        // 수정 시 updatedAt 처리도 가능
    }

    /**
     * 카테고리 삭제
     */
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("해당 카테고리를 찾을 수 없습니다."));

        categoryRepository.delete(category);
    }

    private CategoryResponse toResponse(Category c) {
        return CategoryResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
