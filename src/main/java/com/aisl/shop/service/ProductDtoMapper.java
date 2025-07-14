package com.aisl.shop.service;

import com.aisl.shop.dto.response.product.ProductOptionResponse;
import com.aisl.shop.dto.response.product.ProductResponse;
import com.aisl.shop.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDtoMapper {

    public ProductResponse toDto(Product product) {
        List<ProductOptionResponse> optionResponses = product.getOptions().stream()
                .map(opt -> ProductOptionResponse.builder()
                        .id(opt.getId())
                        .productId(product.getId())
                        .color(opt.getColor())
                        .size(opt.getSize())
                        .stock(opt.getStock())
                        .createdAt(opt.getCreatedAt())
                        .build())
                .toList();

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .thumbnailUrl(product.getThumbnailUrl())
                .createdAt(product.getCreatedAt())
                .options(optionResponses)
                .build();
    }
}
