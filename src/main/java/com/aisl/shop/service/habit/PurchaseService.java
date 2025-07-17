package com.aisl.shop.service.habit;

import com.aisl.shop.dto.request.habit.PurchaseRequest;
import com.aisl.shop.entity.Purchase;
import com.aisl.shop.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    public void createPurchase(Long userId, PurchaseRequest request) {
        Purchase purchase = Purchase.builder()
                .userId(userId)
                .amount(request.getAmount())
                .category(request.getCategory())
                .description(request.getDescription()) // ← 추가되었다면 포함
                .purchaseDate(request.getPurchaseDate()) // ← 날짜 필드
                .yearMonth(request.getYearMonth()) // ← 월별 소비 집계용 필드
                .build();

        purchaseRepository.save(purchase);
    }
}
