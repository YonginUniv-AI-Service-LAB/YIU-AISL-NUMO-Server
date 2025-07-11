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

    private Long userId;

    private Integer amount;

    private String category; // 예: "상의", "하의", "신발"

    private String yearMonth; // 예: "2025-07"
}
