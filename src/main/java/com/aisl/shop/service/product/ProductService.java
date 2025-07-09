package com.aisl.shop.service.product;

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

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
        return toDto(product);
    }

    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategory_Id(categoryId).stream()
                .map(this::toDto)
                .toList();
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.findByNameContaining(keyword).stream()
                .map(this::toDto)
                .toList();
    }

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

        // 옵션도 포함한다면 여기에 옵션 생성 로직 포함 가능
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
