package com.aisl.shop.controller.product;

import com.aisl.shop.dto.response.product.ProductCreateResponse;
import com.aisl.shop.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    /**
     * 전체 상품 조회 또는 키워드 검색
     * GET /products?search=키워드
     */
    @GetMapping
    public List<ProductCreateResponse> getAllProducts(
            @RequestParam(value = "search", required = false) String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return productService.getAllProducts();
        }
        return productService.searchProducts(keyword.trim());
    }

    /**
     * 상품 상세 조회
     * GET /products/{id}
     */
    @GetMapping("/{id}")
    public ProductCreateResponse getProduct(@PathVariable Long id) {
        return productService.getProduct(id); // ProductNotFoundException 처리됨
    }

    /**
     * 카테고리별 상품 조회
     * GET /products/category/{categoryId}
     */
    @GetMapping("/category/{categoryId}")
    public List<ProductCreateResponse> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }
}
