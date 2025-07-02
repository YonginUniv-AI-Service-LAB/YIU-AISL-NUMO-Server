package com.aisl.shop.controller.product;

import com.aisl.shop.dto.request.product.ProductOptionRequest;
import com.aisl.shop.dto.response.product.ProductOptionResponse;
import com.aisl.shop.service.ProductOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductOptionController {

    private final ProductOptionService productOptionService;

    @GetMapping("/products/{productId}/options")
    public List<ProductOptionResponse> getOptions(@PathVariable Long productId) {
        return productOptionService.getOptionsByProductId(productId);
    }

    @PostMapping("/products/{productId}/options")
    public ProductOptionResponse addOption(@PathVariable Long productId,
                                           @RequestBody ProductOptionRequest request) {
        return productOptionService.addOption(productId, request);
    }

    @PatchMapping("/product-options/{optionId}")
    public ProductOptionResponse updateStock(@PathVariable Long optionId,
                                             @RequestParam Integer stock) {
        return productOptionService.updateStock(optionId, stock);
    }

    @DeleteMapping("/product-options/{optionId}")
    public void deleteOption(@PathVariable Long optionId) {
        productOptionService.deleteOption(optionId);
    }
}
