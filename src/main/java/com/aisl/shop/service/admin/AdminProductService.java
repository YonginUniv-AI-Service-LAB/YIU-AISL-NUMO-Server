package com.aisl.shop.service.admin;

import com.aisl.shop.dto.request.product.ProductOptionRequest;
import com.aisl.shop.dto.request.product.ProductRequest;
import com.aisl.shop.dto.response.product.ProductOptionResponse;
import com.aisl.shop.dto.response.product.ProductResponse;
import com.aisl.shop.entity.Category;
import com.aisl.shop.entity.Product;
import com.aisl.shop.entity.ProductOption;
import com.aisl.shop.repository.CategoryRepository;
import com.aisl.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.aisl.shop.util.DiscountCalculator.*;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        updateEntity(product, request);
        return toDto(productRepository.save(product));
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
        updateEntity(product, request);
        return toDto(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private void updateEntity(Product product, ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setCategory(category);
        product.setThumbnailUrl(request.getThumbnailUrl());
        product.setBrand(request.getBrand());
        product.setKeywords(request.getKeywords());
        product.setImageUrls(request.getImageUrls());

        // 할인 계산 로직
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

        // 옵션 설정
        if (request.getOptions() != null) {
            List<ProductOption> options = request.getOptions().stream()
                    .map(optReq -> ProductOption.builder()
                            .product(product)
                            .color(optReq.getColor())
                            .size(optReq.getSize())
                            .stock(optReq.getStock())
                            .build())
                    .collect(Collectors.toList());
            product.setOptions(options);
        }
    }

    private ProductResponse toDto(Product product) {
        List<ProductOptionResponse> optionResponses = product.getOptions().stream()
                .map(option -> ProductOptionResponse.builder()
                        .id(option.getId())
                        .productId(product.getId())
                        .color(option.getColor())
                        .size(option.getSize())
                        .stock(option.getStock())
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
