package com.aisl.shop.controller.admin;

import com.aisl.shop.dto.request.product.ProductCreateRequest;
import com.aisl.shop.dto.response.product.ProductCreateResponse;
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
    public ProductCreateResponse createProduct(@RequestBody @Valid ProductCreateRequest request) {
        return adminProductService.createProduct(request);
    }

    @PatchMapping("/{id}")
    public ProductCreateResponse updateProduct(@PathVariable Long id,
                                               @RequestBody @Valid ProductCreateRequest request) {
        return adminProductService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        adminProductService.deleteProduct(id);
    }
}
