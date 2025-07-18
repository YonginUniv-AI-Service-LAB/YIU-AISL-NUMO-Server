package com.aisl.shop.controller.admin;

import com.aisl.shop.dto.request.product.ProductOptionRequest;
import com.aisl.shop.dto.response.product.ProductOptionResponse;
import com.aisl.shop.service.admin.AdminProductOptionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/products/{productId}/options")
@Validated
public class AdminProductOptionController {

    private final AdminProductOptionService adminProductOptionService;

    // 옵션 등록
    @PostMapping
    public ResponseEntity<ProductOptionResponse> addOption(
            @PathVariable @NotNull(message = "상품 ID는 필수입니다.") Long productId,
            @RequestBody @Valid ProductOptionRequest request) {

        ProductOptionResponse response = adminProductOptionService.addOption(productId, request);
        return ResponseEntity.ok(response);
    }

    // 옵션 삭제
    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(
            @PathVariable @NotNull(message = "상품 ID는 필수입니다.") Long productId,
            @PathVariable @NotNull(message = "옵션 ID는 필수입니다.") Long optionId) {

        adminProductOptionService.deleteOption(optionId);
        return ResponseEntity.ok().build();
    }
}
