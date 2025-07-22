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

    // 코드 (1~5)
    @Column(name = "category_code", nullable = false)
    private int categoryCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    public void setCategoryType(CategoryType type) {
        this.categoryCode = type.getCode();
        this.name = type.getLabel();
    }

    public CategoryType getCategoryType() {
        return CategoryType.fromCode(this.categoryCode);
    }
}
