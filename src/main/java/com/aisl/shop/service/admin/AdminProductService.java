package com.aisl.shop.service.admin;

import com.aisl.shop.dto.request.product.ProductRequest;
import com.aisl.shop.dto.response.product.ProductOptionResponse;
import com.aisl.shop.dto.response.product.ProductResponse;
import com.aisl.shop.entity.Category;
import com.aisl.shop.entity.Product;
import com.aisl.shop.entity.ProductOption;
import com.aisl.shop.exception.admin.ProductNotFoundException;
import com.aisl.shop.exception.admin.CategoryNotFoundException;
import com.aisl.shop.repository.CategoryRepository;
import com.aisl.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.aisl.shop.util.DiscountCalculator.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // ✅ 상품 등록
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        updateEntity(product, request);
        Product savedProduct = productRepository.save(product);
        return toDto(savedProduct);
    }

    // ✅ 상품 수정
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        updateEntity(product, request);
        Product updatedProduct = productRepository.save(product);
        return toDto(updatedProduct);
    }

    // ✅ 상품 삭제
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }

    // ✅ 상품 정보 + 옵션 업데이트
    private void updateEntity(Product product, ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.getCategoryId()));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setCategory(category);
        product.setThumbnailUrl(request.getThumbnailUrl());
        product.setBrand(request.getBrand());
        product.setKeywords(request.getKeywords());
        product.setImageUrls(request.getImageUrls());

        // 할인 계산
        Integer price = request.getPrice();
        Integer discountRate = request.getDiscountRate();
        Integer discountPrice = request.getDiscountPrice();

        if (price != null && discountRate != null) {
            discountPrice = calculateDiscountPrice(price, discountRate);
        } else if (price != null && discountPrice != null) {
            discountRate = calculateDiscountRate(price, discountPrice);
        }

        product.setDiscountRate(discountRate);
        product.setDiscountPrice(discountPrice);

        // 옵션 재설정
        product.clearOptions();

        List<ProductOption> options = request.getOptions().stream()
                .map(optReq -> ProductOption.builder()
                        .product(product)
                        .color(optReq.getColor())
                        .size(optReq.getSize())
                        .stock(optReq.getStock())
                        .additionalPrice(optReq.getAdditionalPrice())
                        .build())
                .collect(Collectors.toList());

        product.addOptions(options);
    }

    // ✅ DTO 변환
    private ProductResponse toDto(Product product) {
        List<ProductOptionResponse> optionResponses = product.getOptions().stream()
                .map(option -> ProductOptionResponse.builder()
                        .id(option.getId())
                        .productId(product.getId())
                        .color(option.getColor())
                        .size(option.getSize())
                        .stock(option.getStock())
                        .additionalPrice(option.getAdditionalPrice())
                        .createdAt(option.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return ProductResponse.builder()
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
