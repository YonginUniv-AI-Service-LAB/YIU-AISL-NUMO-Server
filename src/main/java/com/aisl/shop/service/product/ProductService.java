package com.aisl.shop.service.product;

import com.aisl.shop.dto.response.product.ProductCreateResponse;
import com.aisl.shop.entity.Product;
import com.aisl.shop.exception.product.ProductNotFoundException;
import com.aisl.shop.repository.ProductRepository;
import com.aisl.shop.service.ProductDtoMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDtoMapper productDtoMapper;

    /**
     * 전체 상품 목록 조회
     */
    public List<ProductCreateResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productDtoMapper::toDto)
                .toList();
    }

    /**
     * 상품 단건 조회
     */
    public ProductCreateResponse getProduct(Long id) {
        Product product = productRepository.findByIdWithOptionsAndSizes(id) // 🔥 핵심 수정!
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productDtoMapper.toDto(product); // 이제 sizes 안의 id까지 모두 들어감!
    }

    /**
     * 카테고리별 상품 목록 조회
     */
    public List<ProductCreateResponse> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategory_Id(categoryId).stream()
                .map(productDtoMapper::toDto)
                .toList();
    }

    /**
     * 키워드로 상품 검색
     */
    public List<ProductCreateResponse> searchProducts(String keyword) {
        return productRepository.findByNameContaining(keyword).stream()
                .map(productDtoMapper::toDto)
                .toList();
    }
}
