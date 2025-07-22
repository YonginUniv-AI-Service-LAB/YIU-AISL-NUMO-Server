package com.aisl.shop.enums.review;

import lombok.Getter;

@Getter
public enum ColorOpinion {
    DARK("어두워요"),
    SAME("화면과 같아요"),
    BRIGHT("밝아요");

    private final String label;

    ColorOpinion(String label) {
        this.label = label;
    }
}
