package com.aisl.shop.service;

import com.aisl.shop.dto.request.product.ProductOptionRequest;
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

    public List<ProductOptionResponse> getOptionsByProductId(Long productId) {
        return productOptionRepository.findByProductId(productId).stream()
                .map(this::toDto)
                .toList();
    }

    public ProductOptionResponse addOption(Long productId, ProductOptionRequest request) {
        ProductOption option = new ProductOption();
        option.setProductId(productId);
        option.setColor(request.getColor());
        option.setSize(request.getSize());
        option.setStock(request.getStock());
        return toDto(productOptionRepository.save(option));
    }

    public ProductOptionResponse updateStock(Long optionId, Integer stock) {
        ProductOption option = productOptionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("해당 옵션을 찾을 수 없습니다."));
        option.setStock(stock);
        return toDto(productOptionRepository.save(option));
    }

    public void deleteOption(Long optionId) {
        productOptionRepository.deleteById(optionId);
    }

    private ProductOptionResponse toDto(ProductOption option) {
        return ProductOptionResponse.builder()
                .id(option.getId())
                .productId(option.getProductId())
                .color(option.getColor())
                .size(option.getSize())
                .stock(option.getStock())
                .createdAt(option.getCreatedAt())
                .build();
    }
}
