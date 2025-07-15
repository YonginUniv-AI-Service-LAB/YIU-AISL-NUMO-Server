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

    public void createPurchase(PurchaseRequest request) {
        Purchase purchase = Purchase.builder()
                .userId(request.getUserId())
                .amount(request.getAmount())
                .category(request.getCategory())
                .yearMonth(request.getYearMonth())
                .build();

        purchaseRepository.save(purchase);
    }
}
