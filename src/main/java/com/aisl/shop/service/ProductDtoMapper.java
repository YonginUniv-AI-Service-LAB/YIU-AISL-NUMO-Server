package com.aisl.shop.service;

import com.aisl.shop.dto.response.product.ProductCreateResponse;
import com.aisl.shop.dto.response.product.ProductOptionResponse;
import com.aisl.shop.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDtoMapper {

    public ProductCreateResponse toDto(Product product) {
        List<ProductOptionResponse> optionResponses = product.getOptions().stream()
                .map(opt -> ProductOptionResponse.builder()
                        .id(opt.getId())
                        .productId(product.getId())
                        .color(opt.getColor())
                        // .size(opt.getSize()) → ProductOption에 없다면 주석 처리 또는 제거
                        // .stock(opt.getStock()) → 제거됨
                        .createdAt(opt.getCreatedAt())
                        .build())
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
