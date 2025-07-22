package com.aisl.shop.service;

import com.aisl.shop.dto.response.product.ProductCreateResponse;
import com.aisl.shop.dto.response.product.ProductOptionResponse;
import com.aisl.shop.dto.response.product.ProductSizeResponse;
import com.aisl.shop.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDtoMapper {

    public ProductCreateResponse toDto(Product product) {
        List<ProductOptionResponse> optionResponses = product.getOptions().stream()
                .map(opt -> {
                    // ✅ 각 옵션의 사이즈 리스트 변환
                    List<ProductSizeResponse> sizeResponses = opt.getSizes().stream()
                            .map(size -> ProductSizeResponse.builder()
                                    .id(size.getId()) // 🔥 이 줄이 핵심!
                                    .size(size.getSize())
                                    .createdAt(size.getCreatedAt())
                                    .build())
                            .toList();

                    // ✅ 옵션 생성 시 사이즈도 함께 넣기
                    return ProductOptionResponse.builder()
                            .id(opt.getId())
                            .productId(product.getId())
                            .color(opt.getColor())
                            .createdAt(opt.getCreatedAt())
                            .sizes(sizeResponses) // 🔥 필수!
                            .build();
                })
                .toList();

        return ProductCreateResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .thumbnailUrl(product.getThumbnailUrl())
                .createdAt(product.getCreatedAt())
                .brand(product.getBrand())
                .discountRate(product.getDiscountRate())
                .discountPrice(product.getDiscountPrice())
                .keywords(product.getKeywords())
                .imageUrls(product.getImageUrls())
                .options(optionResponses)
                .build();
    }
}
