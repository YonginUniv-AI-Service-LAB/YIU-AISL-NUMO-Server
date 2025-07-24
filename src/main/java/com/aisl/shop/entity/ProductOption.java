package com.aisl.shop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;
    // N:1 연관관계 설정 (상품 하나에 여러 색상 옵션)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference // 🔥 추가!
    private Product product;

    // 색상
    @Column(nullable = false, length = 50)
    private String color;

    // 색상마다 여러 사이즈가 존재함
    @OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSize> sizes = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    // ✅ 연관관계 편의 메서드 추가
    public void addSize(ProductSize size) {
        sizes.add(size);
        size.setProductOption(this);
    }

    public void addSizes(List<ProductSize> sizes) {
        sizes.forEach(this::addSize); // 반복해서 추가
    }
}