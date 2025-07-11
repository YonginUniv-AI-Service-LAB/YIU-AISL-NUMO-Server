package com.aisl.shop.enums;

import lombok.Getter;

@Getter
public enum SizeOpinion {
    SMALL("작아요"),
    NORMAL("딱 맞아요"),
    LARGE("커요");

    private final String label;

    SizeOpinion(String label) {
        this.label = label;
    }
}
