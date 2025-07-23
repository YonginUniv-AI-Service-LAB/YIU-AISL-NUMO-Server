package com.aisl.shop.enums;

import java.util.Arrays;

public enum CategoryType {
    TOP("TOP", "상의"),
    BOTTOM("BOTTOM", "하의"),
    OUTER("OUTER", "아우터"),
    SHOES("SHOES", "신발"),
    ACC("ACC", "ACC");

    private final String code;
    private final String label;

    CategoryType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static CategoryType fromCode(String code) {
        return Arrays.stream(CategoryType.values())
                .filter(t -> t.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid category code: " + code));
    }
}
