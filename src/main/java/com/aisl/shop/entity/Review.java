package com.aisl.shop.entity;

import com.aisl.shop.enums.review.ColorOpinion;
import com.aisl.shop.enums.review.QualityOpinion;
import com.aisl.shop.enums.review.SizeOpinion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long productId;

    @Column(nullable = false)
    private int rating; // 1~5

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SizeOpinion sizeOpinion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ColorOpinion colorOpinion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QualityOpinion qualityOpinion;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ElementCollection
    @CollectionTable(name = "review_images", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
