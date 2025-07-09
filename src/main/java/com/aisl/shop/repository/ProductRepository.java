package com.aisl.shop.repository;

import com.aisl.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //  연관 엔티티(Category)의 id 기준 조회
    List<Product> findByCategory_Id(Long categoryId);

    //  상품명 키워드 검색 (그대로 사용 가능)
    List<Product> findByNameContaining(String keyword);
}
