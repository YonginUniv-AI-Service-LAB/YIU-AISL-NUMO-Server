package com.aisl.shop.controller.product;

import com.aisl.shop.dto.response.product.ProductOptionResponse;
import com.aisl.shop.service.product.ProductOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/{productId}/options")
public class ProductOptionController {

    private final ProductOptionService productOptionService;

    // 옵션 조회 (사용자용)
    @GetMapping
    public List<ProductOptionResponse> getOptions(@PathVariable Long productId) {
        return productOptionService.getOptionsByProductId(productId);
    }
}
