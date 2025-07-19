package com.aisl.shop.service.admin;

import com.aisl.shop.dto.request.product.ProductCreateRequest;
import com.aisl.shop.dto.response.product.ProductCreateResponse;
import com.aisl.shop.dto.response.product.ProductOptionResponse;
import com.aisl.shop.dto.response.product.ProductSizeResponse;
import com.aisl.shop.entity.Category;
import com.aisl.shop.entity.Product;
import com.aisl.shop.entity.ProductOption;
import com.aisl.shop.entity.ProductSize;
import com.aisl.shop.exception.category.CategoryNotFoundException;
import com.aisl.shop.repository.CategoryRepository;
import com.aisl.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * ✅ 상품 등록
     */
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

        List<ProductOption> optionList = new ArrayList<>();

        for (String color : request.getColors()) {
            if (color == null || color.trim().isEmpty()) continue;

            ProductOption option = ProductOption.builder()
                    .color(color.trim())
                    .product(product)
                    .sizes(new ArrayList<>())
                    .build();

            for (String sizeStr : request.getSizes()) {
                if (sizeStr == null || sizeStr.trim().isEmpty()) continue;

                ProductSize size = ProductSize.builder()
                        .size(sizeStr.trim())
                        .productOption(option)
                        .build();
                option.getSizes().add(size);
            }

            optionList.add(option);
        }

        product.addOptions(optionList);

        Product savedProduct = productRepository.save(product);
        return toResponse(savedProduct);
    }

    /**
     * ✅ 상품 수정
     */
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

        product.clearOptions();

        List<ProductOption> optionList = new ArrayList<>();

        for (String color : request.getColors()) {
            if (color == null || color.trim().isEmpty()) continue;

            ProductOption option = ProductOption.builder()
                    .color(color.trim())
                    .product(product)
                    .sizes(new ArrayList<>())
                    .build();

            for (String sizeStr : request.getSizes()) {
                if (sizeStr == null || sizeStr.trim().isEmpty()) continue;

                ProductSize size = ProductSize.builder()
                        .size(sizeStr.trim())
                        .productOption(option)
                        .build();
                option.getSizes().add(size);
            }

            optionList.add(option);
        }

        product.addOptions(optionList);

        Product updatedProduct = productRepository.save(product);
        return toResponse(updatedProduct);
    }

    /**
     * ✅ 상품 삭제
     */
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. id=" + id));
        productRepository.delete(product);
    }

    /**
     * ✅ 응답 DTO 변환
     */
    private ProductCreateResponse toResponse(Product product) {
        List<ProductOptionResponse> optionResponses = product.getOptions().stream()
                .map(opt -> ProductOptionResponse.builder()
                        .id(opt.getId())
                        .productId(product.getId())
                        .color(opt.getColor())
                        .sizes(opt.getSizes().stream()
                                .map(size -> ProductSizeResponse.builder()
                                        .id(size.getId())
                                        .size(size.getSize())
                                        .createdAt(size.getCreatedAt())
                                        .build())
                                .toList())
                        .createdAt(opt.getCreatedAt())
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
