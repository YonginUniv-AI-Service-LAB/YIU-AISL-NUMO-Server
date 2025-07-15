package com.aisl.shop.repository;

import com.aisl.shop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 이름 중복 확인
     */
    boolean existsByName(String name);
}
