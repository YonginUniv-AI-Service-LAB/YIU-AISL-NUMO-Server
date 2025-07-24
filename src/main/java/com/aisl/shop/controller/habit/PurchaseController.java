package com.aisl.shop.controller.habit;

import com.aisl.shop.config.CustomUserDetails;
import com.aisl.shop.dto.request.habit.PurchaseRequest;
import com.aisl.shop.dto.response.habit.PurchaseResponse;
import com.aisl.shop.service.habit.PurchaseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habits/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    /**
     * 🔹 소비 내역 등록
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPurchase(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PurchaseRequest request
    ) {
        Long userId = userDetails.getId(); // 🔹 JWT에서 추출한 사용자 ID
        purchaseService.createPurchase(userId, request);
    }

    /**
     * 🔹 특정 월 소비 내역 전체 조회
     */
    @GetMapping
    public ResponseEntity<List<PurchaseResponse>> getPurchasesByMonth(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("yearMonth")
            @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])$", message = "yearMonth는 YYYY-MM 형식이어야 합니다.")
            String yearMonth
    ) {
        Long userId = userDetails.getId();
        List<PurchaseResponse> purchases = purchaseService.getPurchasesByMonth(userId, yearMonth);
        return ResponseEntity.ok(purchases);
    }
}
