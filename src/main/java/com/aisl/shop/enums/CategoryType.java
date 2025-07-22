package com.aisl.shop.enums;

public enum CategoryType {
    TOP(1, "상의"),
    BOTTOM(2, "하의"),
    OUTER(3, "아우터"),
    SHOES(4, "신발"),
    ACC(5, "ACC");

    private final int code;
    private final String label;

    CategoryType(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static CategoryType fromCode(int code) {
        for (CategoryType type : values()) {
            if (type.code == code) return type;
        }
        throw new IllegalArgumentException("Invalid Category Code: " + code);
    }

    public static CategoryType fromLabel(String label) {
        for (CategoryType type : values()) {
            if (type.label.equals(label)) return type;
        }
        throw new IllegalArgumentException("Invalid Category Label: " + label);
    }
}
