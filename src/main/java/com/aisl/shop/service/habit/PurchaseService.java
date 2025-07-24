package com.aisl.shop.service.habit;

import com.aisl.shop.dto.request.habit.PurchaseRequest;
import com.aisl.shop.dto.response.habit.PurchaseResponse;
import com.aisl.shop.entity.Purchase;
import com.aisl.shop.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    /**
     * 🔹 소비 내역 등록
     */
    public void createPurchase(Long userId, PurchaseRequest request) {
        Purchase purchase = Purchase.builder()
                .userId(userId)
                .amount(request.getAmount())
                .category(request.getCategory())
                .description(request.getDescription())
                .purchaseDate(request.getPurchaseDate())
                .yearMonth(request.getYearMonth())
                .build();

        purchaseRepository.save(purchase);
    }

    /**
     * 🔹 특정 월 소비 내역 조회
     */
    public List<PurchaseResponse> getPurchasesByMonth(Long userId, String yearMonth) {
        List<Purchase> purchases = purchaseRepository.findAllByUserIdAndYearMonth(userId, yearMonth);

        return purchases.stream()
                .map(purchase -> PurchaseResponse.builder()
                        .id(purchase.getId())
                        .amount(purchase.getAmount())
                        .category(purchase.getCategory())
                        .description(purchase.getDescription())
                        .purchaseDate(LocalDate.parse(purchase.getPurchaseDate())) // ✅ 여기 수정
                        .build())

                .collect(Collectors.toList());
    }
}
