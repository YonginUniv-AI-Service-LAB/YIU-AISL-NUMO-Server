package com.aisl.shop.controller.order;

import com.aisl.shop.dto.request.order.OrderCreateRequest;
import com.aisl.shop.dto.request.order.OrderPayRequest;
import com.aisl.shop.dto.response.order.OrderDetailResponse;
import com.aisl.shop.dto.response.order.OrderListItemResponse;
import com.aisl.shop.service.order.OrderService;
import com.aisl.shop.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 🔹 주문 생성
    @PostMapping
    public Long createOrder(@RequestBody @Valid OrderCreateRequest request) {
        return orderService.createOrder(request);
    }


    // 🔹 내 주문 목록 조회 + 상태 및 기간 필터링
    @GetMapping
    public List<OrderListItemResponse> getMyOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Long userId = SecurityUtil.getCurrentUserId(); // ✅ 변경됨
        return orderService.getFilteredOrderList(userId, status, startDate, endDate);
    }



    // 🔹 주문 상세 조회
    @GetMapping("/{orderId}")
    public OrderDetailResponse getOrder(@PathVariable Long orderId) {
        return orderService.getOrderDetail(orderId);
    }

    // 🔹 주문 전체 취소 (결제 전만 가능)
    @PatchMapping("/{orderId}/cancel")
    public void cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
    }

    // 🔹 결제 처리 (Mock 기반)
    @PatchMapping("/{orderId}/pay")
    public void payOrder(@PathVariable Long orderId, @RequestBody OrderPayRequest request) {
        orderService.payOrder(orderId, request);
    }

    // 🔹 구매 확정
    @PatchMapping("/{orderId}/confirm")
    public void confirmOrder(@PathVariable Long orderId) {
        orderService.confirmOrder(orderId);
    }

    // ✅ 주문상품 반품 요청
    @PatchMapping("/items/{itemId}/return")
    public void returnOrderItem(@PathVariable Long itemId) {
        orderService.returnOrderItem(itemId);
    }

    // ✅ 주문상품 교환 요청
    @PatchMapping("/items/{itemId}/exchange")
    public void exchangeOrderItem(@PathVariable Long itemId) {
        orderService.exchangeOrderItem(itemId);
    }

    // ✅ 주문상품 취소 요청
    @PatchMapping("/items/{itemId}/cancel")
    public void cancelOrderItem(@PathVariable Long itemId) {
        orderService.cancelOrderItem(itemId);
    }
}
