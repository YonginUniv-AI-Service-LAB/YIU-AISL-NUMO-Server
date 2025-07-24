package com.aisl.shop.dto.response.order;

import com.aisl.shop.entity.Order.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    // :흰색_확인_표시: 추가!
    private List<ItemSummary> items;
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ItemSummary {
        private String productName;
        private String optionName;
        private Integer quantity;
    }
}
