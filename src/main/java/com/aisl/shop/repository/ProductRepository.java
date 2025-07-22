package com.aisl.shop.repository;

import com.aisl.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 카테고리 기준 조회
    List<Product> findByCategory_Id(Long categoryId);

    // 상품명 키워드 검색
    List<Product> findByNameContaining(String keyword);

    // ✅ 옵션과 사이즈까지 한 번에 가져오는 쿼리
    @Query("SELECT p FROM Product p " +
            "JOIN FETCH p.options o " +
//            "JOIN FETCH o.sizes " +
            "WHERE p.id = :id")
    Optional<Product> findByIdWithOptionsAndSizes(@Param("id") Long id);
}
