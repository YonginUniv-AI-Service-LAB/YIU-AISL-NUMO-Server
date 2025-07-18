package com.aisl.shop.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionRequest {

    @NotBlank(message = "색상을 입력해주세요.")
    private String color;

    /**
     * 사이즈 리스트 예: ["S", "M", "L", "XL"]
     */
    @NotNull(message = "사이즈 리스트는 비어 있을 수 없습니다.")
    private List<String> sizes;

    /**
     * ❌ getSize()는 존재하지 않습니다.
     * ✅ 반드시 getSizes()를 사용하세요.
     *
     * 예시:
     * for (String size : request.getSizes()) { ... }
     */
}
