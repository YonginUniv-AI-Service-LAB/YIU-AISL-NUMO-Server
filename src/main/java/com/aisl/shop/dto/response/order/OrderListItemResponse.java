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
    // :별:️ 주문상품 요약 리스트 추가!
    private List<ItemSummary> items;
    // :별:️ 주문상품 요약 정보 (상품명, 옵션명, 수량)
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemSummary {
        private String productName;
        private String optionName;
        private Integer quantity;
    }
}









