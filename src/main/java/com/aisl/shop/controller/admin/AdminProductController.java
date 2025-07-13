package com.aisl.shop.controller.admin;

import com.aisl.shop.dto.request.product.ProductRequest;
import com.aisl.shop.dto.response.product.ProductResponse;
import com.aisl.shop.service.admin.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    /**
     * 상품 등록 API (관리자용)
     * - 요청 본문: ProductRequest (상품명, 가격, 설명 등)
     * - 응답: 등록된 상품 정보 반환
     * - URL: POST /admin/products
     */
    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductRequest request) {
        return adminProductService.createProduct(request);
    }

    /**
     * 상품 수정 API (관리자용)
     * - 경로 변수: 상품 ID
     * - 요청 본문: ProductRequest (수정할 정보 포함)
     * - 응답: 수정된 상품 정보 반환
     * - URL: PATCH /admin/products/{id}
     */
    @PatchMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        return adminProductService.updateProduct(id, request);
    }

    /**
     * 상품 삭제 API (관리자용)
     * - 경로 변수: 상품 ID
     * - 응답: 없음 (void)
     * - URL: DELETE /admin/products/{id}
     */
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        adminProductService.deleteProduct(id);
    }
}
