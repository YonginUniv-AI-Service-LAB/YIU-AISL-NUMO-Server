package com.aisl.shop.service.product;

import com.aisl.shop.dto.request.product.ProductOptionRequest;
import com.aisl.shop.dto.response.product.ProductOptionResponse;
import com.aisl.shop.entity.Product;
import com.aisl.shop.entity.ProductOption;
import com.aisl.shop.repository.ProductOptionRepository;
import com.aisl.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOptionService {

    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;

    /**
     * ✅ 특정 상품의 옵션 목록 조회
     */
    public List<ProductOptionResponse> getOptionsByProductId(Long productId) {
        return productOptionRepository.findByProduct_Id(productId).stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * ✅ 상품 옵션 등록
     */
    public ProductOptionResponse addOption(Long productId, ProductOptionRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("해당 상품을 찾을 수 없습니다."));

        ProductOption option = ProductOption.builder()
                .product(product)
                .color(request.getColor())
                .size(request.getSize())
                .stock(request.getStock())
                .build();

        return toDto(productOptionRepository.save(option));
    }

    /**
     * ✅ 재고 수량 수정
     */
    public ProductOptionResponse updateStock(Long optionId, Integer stock) {
        ProductOption option = productOptionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("해당 옵션을 찾을 수 없습니다."));
        option.setStock(stock);
        return toDto(productOptionRepository.save(option));
    }

    /**
     * ✅ 옵션 삭제
     */
    public void deleteOption(Long optionId) {
        productOptionRepository.deleteById(optionId);
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
