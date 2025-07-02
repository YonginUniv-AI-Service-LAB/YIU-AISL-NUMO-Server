package com.aisl.shop.dto.response.order;

import com.aisl.shop.entity.Order.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderListItemResponse {
    private Long orderId;
    private OrderStatus status;
    private Integer totalPrice;
    private LocalDateTime createdAt;
}
