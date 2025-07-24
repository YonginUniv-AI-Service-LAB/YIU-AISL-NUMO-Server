package com.aisl.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 ID
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 상품명 (스냅샷용)
    @Column(name = "product_name", nullable = false)
    private String productName;

    // 브랜드명 (스냅샷용)
    @Column(name = "brand_name", nullable = false)
    private String brandName;

    // 상품 ID (원본 참조용)
    @Column(name = "product_id", nullable = false)
    private Long productId;

    // 색상
    @Column(nullable = false)
    private String color;

    // 사이즈
    @Column(nullable = false)
    private String size;

    // 수량
    @Column(nullable = false)
    private Integer quantity;

    // 단가 (옵션이 있다면 옵션 가격을 넣음)
    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

    // 총 가격 = 단가 * 수량
    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    // 장바구니 담은 시점
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
