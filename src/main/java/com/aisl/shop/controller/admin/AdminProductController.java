package com.aisl.shop.controller.admin;

import com.aisl.shop.dto.request.product.ProductRequest;
import com.aisl.shop.dto.response.product.ProductResponse;
import com.aisl.shop.service.admin.AdminProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    @PostMapping
    public ProductResponse createProduct(@RequestBody @Valid ProductRequest request) {
        return adminProductService.createProduct(request);
    }

    @PatchMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequest request) {
        return adminProductService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        adminProductService.deleteProduct(id);
    }
}
