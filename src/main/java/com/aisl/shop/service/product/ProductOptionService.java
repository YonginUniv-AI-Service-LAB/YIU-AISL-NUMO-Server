package com.aisl.shop.service.product;

import com.aisl.shop.dto.response.product.ProductOptionResponse;
import com.aisl.shop.entity.ProductOption;
import com.aisl.shop.repository.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOptionService {

    private final ProductOptionRepository productOptionRepository;

    /**
     * ✅ 특정 상품의 옵션 목록 조회 (사용자용)
     */
    public List<ProductOptionResponse> getOptionsByProductId(Long productId) {
        return productOptionRepository.findByProduct_Id(productId).stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * ✅ Entity → Response DTO 변환
     */
    private ProductOptionResponse toDto(ProductOption option) {
        return ProductOptionResponse.builder()
                .id(option.getId())
                .productId(option.getProduct().getId())
                .color(option.getColor())
                .size(option.getSize())
                .stock(option.getStock())
                .createdAt(option.getCreatedAt())
                .build();
    }
}
