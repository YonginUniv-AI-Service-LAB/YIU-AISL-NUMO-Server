package com.aisl.shop.controller.cartitem;

import com.aisl.shop.dto.request.cartitem.CartItemRequest;
import com.aisl.shop.dto.response.cartitem.CartItemResponse;
import com.aisl.shop.service.cartitem.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // TODO: 로그인 연동되면 userId는 JWT에서 추출
    private final Long mockUserId = 1L;

    // 장바구니 추가
    @PostMapping("/cart")
    public CartItemResponse addToCart(@RequestBody CartItemRequest request) {
        return cartService.addToCart(mockUserId, request);
    }

    // 장바구니 전체 조회
    @GetMapping("/cart")
    public List<CartItemResponse> getCart() {
        return cartService.getCartItems(mockUserId);
    }

    // 장바구니 항목 수정 (옵션 + 수량)
    @PatchMapping("/cart/items/{cartItemId}")
    public CartItemResponse updateCartItem(@PathVariable Long cartItemId,
                                           @RequestBody CartItemRequest request) {
        return cartService.updateCartItem(cartItemId, request);
    }

    // 장바구니 항목 삭제
    @DeleteMapping("/cart/items/{cartItemId}")
    public void removeItem(@PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
    }

    // 장바구니 전체 비우기
    @DeleteMapping("/cart")
    public void clearCart() {
        cartService.clearCart(mockUserId);
    }
}
