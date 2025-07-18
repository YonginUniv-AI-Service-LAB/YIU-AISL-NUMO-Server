package com.aisl.shop.repository;

import com.aisl.shop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 이름 중복 확인
     */
    boolean existsByName(String name);

    /**
     * 이름으로 카테고리 조회
     */
    Optional<Category> findByName(String name);
}
