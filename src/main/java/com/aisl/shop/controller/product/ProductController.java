package com.aisl.shop.controller.product;

import com.aisl.shop.dto.response.product.ProductResponse;
import com.aisl.shop.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    // 전체 조회 or 키워드 검색
    @GetMapping
    public List<ProductResponse> getAllProducts(@RequestParam(value = "search", required = false) String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return productService.getAllProducts();
        }
        return productService.searchProducts(keyword);
    }

    // 상품 상세 조회
    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    // 카테고리별 상품 조회
    @GetMapping("/category/{categoryId}")
    public List<ProductResponse> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }
}
