package com.aisl.shop.dto.response.order;

import com.aisl.shop.entity.Order.OrderStatus;
import com.aisl.shop.entity.Order.PaymentMethod;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponse {
    private Long orderId;
    private String email;
    private String name;
    private String phone;
    private String address;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
    private Integer totalPrice;
    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemResponse {
        private Long itemId;
        private String productName;   // 추후 ProductService에서 조회
        private String optionName;    // 추후 OptionService에서 조회
        private Integer quantity;
        private Integer unitPrice;
        private Integer totalPrice;


    }
}
