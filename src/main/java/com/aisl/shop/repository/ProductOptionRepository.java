package com.aisl.shop.repository;

import com.aisl.shop.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    //  연관 엔티티인 Product의 id로 옵션 목록 조회
    List<ProductOption> findByProduct_Id(Long productId);
}
