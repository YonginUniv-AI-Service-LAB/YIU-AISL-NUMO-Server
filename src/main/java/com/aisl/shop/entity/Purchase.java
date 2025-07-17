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

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false, length = 50)
    private String category; // 예: "상의", "하의", "신발" 등

    @Column(nullable = false, length = 7)
    private String yearMonth; // "2025-07"

    // ✅ description 필드 추가
    @Column(length = 255)
    private String description;

    @Column(length = 10)
    private String purchaseDate; // "YYYY-MM-DD"

}
