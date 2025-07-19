package com.aisl.shop.service.admin;

import com.aisl.shop.dto.request.product.ProductCreateRequest;
import com.aisl.shop.dto.response.product.ProductCreateResponse;
import com.aisl.shop.dto.response.product.ProductOptionResponse;
import com.aisl.shop.dto.response.product.ProductSizeResponse;
import com.aisl.shop.entity.*;
import com.aisl.shop.exception.category.CategoryNotFoundException;
import com.aisl.shop.repository.CategoryRepository;
import com.aisl.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다. ID: " + request.getCategoryId()));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(category)
                .brand(request.getBrand())
                .discountRate(request.getDiscountRate())
                .discountPrice(request.getDiscountPrice())
                .thumbnailUrl(request.getThumbnailUrl())
                .keywords(request.getKeywords())
                .imageUrls(request.getImageUrls())
                .options(new ArrayList<>())
                .build();

        List<ProductOption> optionList = buildOptions(product, request.getColors(), request.getSizes());
        product.addOptions(optionList);

        Product savedProduct = productRepository.save(product);
        return toResponse(savedProduct);
    }

    @Transactional
    public ProductCreateResponse updateProduct(Long id, ProductCreateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. id=" + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다. ID: " + request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(category);
        product.setBrand(request.getBrand());
        product.setDiscountRate(request.getDiscountRate());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setThumbnailUrl(request.getThumbnailUrl());
        product.setKeywords(request.getKeywords());
        product.setImageUrls(request.getImageUrls());

        product.clearOptions(); // 기존 옵션 제거
        List<ProductOption> optionList = buildOptions(product, request.getColors(), request.getSizes());
        product.addOptions(optionList);

        Product updatedProduct = productRepository.save(product);
        return toResponse(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. id=" + id));
        productRepository.delete(product);
    }

    /**
     * 옵션과 사이즈 생성
     */
    private List<ProductOption> buildOptions(Product product, List<String> colors, List<String> sizes) {
        List<ProductOption> optionList = new ArrayList<>();

        for (String rawColor : colors) {
            String color = rawColor.trim();
            if (color.isEmpty()) continue;

            ProductOption option = ProductOption.builder()
                    .color(color)
                    .product(product)
                    .sizes(new ArrayList<>())
                    .build();

            for (String rawSize : sizes) {
                String sizeStr = rawSize.trim();
                if (sizeStr.isEmpty()) continue;

                ProductSize size = ProductSize.builder()
                        .size(sizeStr)
                        .productOption(option)
                        .build();

                option.getSizes().add(size); // 또는 option.addSize(size) 사용 가능
            }

            optionList.add(option);
        }

        return optionList;
    }

    /**
     * 응답 DTO 변환
     */
    private ProductCreateResponse toResponse(Product product) {
        List<ProductOptionResponse> optionResponses = product.getOptions().stream()
                .map(option -> ProductOptionResponse.builder()
                        .id(option.getId())
                        .productId(product.getId())
                        .color(option.getColor())
                        .createdAt(option.getCreatedAt())
                        .sizes(option.getSizes().stream()
                                .map(size -> ProductSizeResponse.builder()
                                        .id(size.getId())
                                        .size(size.getSize())
                                        .createdAt(size.getCreatedAt())
                                        .build())
                                .toList())
                        .build())
                .toList();

        return ProductCreateResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .categoryId(product.getCategory().getId())
                .brand(product.getBrand())
                .discountRate(product.getDiscountRate())
                .discountPrice(product.getDiscountPrice())
                .thumbnailUrl(product.getThumbnailUrl())
                .keywords(product.getKeywords())
                .imageUrls(product.getImageUrls())
                .createdAt(product.getCreatedAt())
                .options(optionResponses)
                .build();
    }
}
