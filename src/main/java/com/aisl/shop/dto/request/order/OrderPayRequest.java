package com.aisl.shop.dto.request.order;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPayRequest {
    private boolean mock; // 실제 결제 없이 처리
}
