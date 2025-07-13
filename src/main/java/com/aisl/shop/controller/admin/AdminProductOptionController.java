package com.aisl.shop.controller.admin;

import com.aisl.shop.dto.request.product.ProductOptionRequest;
import com.aisl.shop.dto.response.product.ProductOptionResponse;
import com.aisl.shop.service.admin.AdminProductOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/products/{productId}/options")
public class AdminProductOptionController {

    private final AdminProductOptionService adminProductOptionService;

    // 옵션 등록
    @PostMapping
    public ProductOptionResponse addOption(@PathVariable Long productId,
                                           @RequestBody ProductOptionRequest request) {
        return adminProductOptionService.addOption(productId, request);
    }

    // 옵션 재고 수정
    @PatchMapping("/{optionId}")
    public ProductOptionResponse updateStock(@PathVariable Long productId,
                                             @PathVariable Long optionId,
                                             @RequestParam Integer stock) {
        return adminProductOptionService.updateStock(optionId, stock);
    }

    // 옵션 삭제
    @DeleteMapping("/{optionId}")
    public void deleteOption(@PathVariable Long productId,
                             @PathVariable Long optionId) {
        adminProductOptionService.deleteOption(optionId);
    }
}
