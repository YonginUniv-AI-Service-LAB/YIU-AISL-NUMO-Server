package com.aisl.shop.service.admin;

import com.aisl.shop.dto.request.product.ProductOptionRequest;
import com.aisl.shop.dto.response.product.ProductOptionResponse;
import com.aisl.shop.dto.response.product.ProductSizeResponse;
import com.aisl.shop.entity.Product;
import com.aisl.shop.entity.ProductOption;
import com.aisl.shop.entity.ProductSize;
import com.aisl.shop.exception.admin.ProductNotFoundException;
import com.aisl.shop.exception.admin.ProductOptionNotFoundException;
import com.aisl.shop.repository.ProductOptionRepository;
import com.aisl.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminProductOptionService {

    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;

    /**
     * ✅ 상품 옵션 등록 (관리자)
     */
    public ProductOptionResponse addOption(Long productId, ProductOptionRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductOption option = ProductOption.builder()
                .product(product)
                .color(request.getColor())
                .sizes(new ArrayList<>())
                .build();

        for (String sizeStr : request.getSizes()) {
            ProductSize size = ProductSize.builder()
                    .size(sizeStr)
                    .productOption(option)
                    .build();
            option.getSizes().add(size);
        }

        ProductOption savedOption = productOptionRepository.save(option);
        return toDto(savedOption);
    }

    /**
     * ✅ 옵션 삭제 (관리자)
     */
    public void deleteOption(Long optionId) {
        ProductOption option = productOptionRepository.findById(optionId)
                .orElseThrow(() -> new ProductOptionNotFoundException(optionId));

        productOptionRepository.delete(option);
    }

    /**
     * ✅ Entity → Response DTO 변환
     */
    private ProductOptionResponse toDto(ProductOption option) {
        List<ProductSizeResponse> sizeResponses = option.getSizes().stream()
                .map(size -> ProductSizeResponse.builder()
                        .id(size.getId())
                        .size(size.getSize())
                        .createdAt(size.getCreatedAt())
                        .build())
                .toList();

        return ProductOptionResponse.builder()
                .id(option.getId())
                .productId(option.getProduct().getId())
                .color(option.getColor())
                .sizes(sizeResponses)
                .createdAt(option.getCreatedAt())
                .build();
    }
}
