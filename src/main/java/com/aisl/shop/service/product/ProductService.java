package com.aisl.shop.service.product;

import com.aisl.shop.dto.response.product.ProductOptionResponse;
import com.aisl.shop.dto.response.product.ProductResponse;
import com.aisl.shop.entity.Product;
import com.aisl.shop.service.helper.ProductDtoMapper;
import com.aisl.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDtoMapper productDtoMapper; // 별도 분리해도 좋음

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productDtoMapper::toDto)
                .toList();
    }

    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
        return productDtoMapper.toDto(product);
    }

    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategory_Id(categoryId).stream()
                .map(productDtoMapper::toDto)
                .toList();
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.findByNameContaining(keyword).stream()
                .map(productDtoMapper::toDto)
                .toList();
    }
}
