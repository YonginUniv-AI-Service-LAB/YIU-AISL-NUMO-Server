package com.aisl.shop.controller.habit;

import com.aisl.shop.dto.request.habit.PurchaseRequest;
import com.aisl.shop.service.habit.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/habits/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPurchase(@Valid @RequestBody PurchaseRequest request) {
        purchaseService.createPurchase(request);
    }
}
