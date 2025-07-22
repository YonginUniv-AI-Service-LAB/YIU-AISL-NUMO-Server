package com.aisl.shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "spending_goals", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "month_key"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpendingGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 실제 DB 컬럼명은 'month_key'로, 충돌 방지
    @Column(name = "month_key", nullable = false, length = 7)
    private String yearMonth;

    @Column(nullable = false)
    private Integer targetAmount;

    @Column(nullable = false)
    @Builder.Default
    private Integer currentSpending = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public double calculateAchievementRate() {
        if (targetAmount == null || targetAmount == 0) return 0.0;
        return Math.min(100.0, (double) currentSpending / targetAmount * 100);
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
