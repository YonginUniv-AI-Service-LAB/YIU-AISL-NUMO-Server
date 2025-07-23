package com.aisl.shop.entity;

import com.aisl.shop.enums.CategoryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이름 (ex: "상의", "하의" 등)
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    // categoryCode: TOP, BOTTOM, OUTER ...
    @Column(name = "category_code", nullable = false, length = 50)
    private String categoryCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    // ENUM → Entity에 반영
    public void setCategoryType(CategoryType type) {
        this.categoryCode = type.getCode();   // 예: "TOP"
        this.name = type.getLabel();          // 예: "상의"
    }

    public CategoryType getCategoryType() {
        return CategoryType.fromCode(this.categoryCode); // 예: CategoryType.TOP
    }
}
