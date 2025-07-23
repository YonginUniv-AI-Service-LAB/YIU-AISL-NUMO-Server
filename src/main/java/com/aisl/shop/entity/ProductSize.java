package com.aisl.shop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_sizes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 색상 옵션 하나에 여러 사이즈 가능 (다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id", nullable = false)
    @JsonBackReference  // 순환 참조 방지
    private ProductOption productOption;

    @Column(nullable = false, length = 20)
    private String size;  // 예: S, M, L, Free

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
