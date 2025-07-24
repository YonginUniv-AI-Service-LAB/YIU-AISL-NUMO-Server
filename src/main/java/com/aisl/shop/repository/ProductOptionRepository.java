package com.aisl.shop.repository;

import com.aisl.shop.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    List<ProductOption> findByProduct_Id(Long productId);

    // ✅ 옵션 존재 여부 확인용
    boolean existsByProduct_Id(Long productId);


}
