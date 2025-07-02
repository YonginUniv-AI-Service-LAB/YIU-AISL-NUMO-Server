package com.aisl.shop.controller.order;

import com.aisl.shop.dto.request.order.OrderCreateRequest;
import com.aisl.shop.dto.request.order.OrderPayRequest;
import com.aisl.shop.dto.response.order.OrderDetailResponse;
import com.aisl.shop.dto.response.order.OrderListItemResponse;
import com.aisl.shop.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Long createOrder(@RequestBody OrderCreateRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping
    public List<OrderListItemResponse> getMyOrders() {
        Long userId = 1L; // 로그인 연동 시 수정
        return orderService.getOrderList(userId);
    }

    @GetMapping("/{orderId}")
    public OrderDetailResponse getOrder(@PathVariable Long orderId) {
        return orderService.getOrderDetail(orderId);
    }

    @PatchMapping("/{orderId}/cancel")
    public void cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
    }

    @PatchMapping("/{orderId}/pay")
    public void payOrder(@PathVariable Long orderId, @RequestBody OrderPayRequest request) {
        orderService.payOrder(orderId, request);
    }
}
