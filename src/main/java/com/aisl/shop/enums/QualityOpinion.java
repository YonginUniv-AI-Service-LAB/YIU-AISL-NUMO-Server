package com.aisl.shop.enums;

import lombok.Getter;

@Getter
public enum QualityOpinion {
    BAD("별로예요"),
    AVERAGE("괜찮아요"),
    GOOD("아주 좋아요");

    private final String label;

    QualityOpinion(String label) {
        this.label = label;
    }
}
