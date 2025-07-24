package com.aisl.shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔸 사용자 ID (FK로 연결 가능성 있음)
    @Column(nullable = false)
    private Long userId;

    // 🔸 지출 금액
    @Column(nullable = false)
    private Integer amount;

    // 🔸 소비 카테고리 (예: 식비, 교통, 의류 등)
    @Column(nullable = false, length = 50)
    private String category;

    // 🔸 소비 발생 연월 (집계 용도) — "2025-07"
    @Column(name = "year_month", nullable = false, length = 7)
    private String yearMonth;

    // 🔸 상세 설명 (optional)
    @Column(length = 255)
    private String description;

    // 🔸 소비 발생일 (예: 2025-07-24) — 나중에 LocalDate로 바꾸는 것도 고려 가능
    @Column(name = "purchase_date", nullable = false, length = 10)
    private String purchaseDate;
}
