package com.aisl.shop.controller.cartitem;

import com.aisl.shop.dto.request.cartitem.CartItemRequest;
import com.aisl.shop.dto.response.cartitem.CartItemResponse;
import com.aisl.shop.service.cartitem.CartitemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartItemController {

    private final CartitemService cartService;

    // TODO: 로그인 연동되면 JWT에서 userId 추출
    private final Long mockUserId = 1L;

    /**
     * ✅ 장바구니 항목 추가
     */
    @PostMapping
    public CartItemResponse addToCart(@RequestBody @Valid CartItemRequest request) {
        return cartService.addToCart(mockUserId, request);
    }

    /**
     * ✅ 장바구니 전체 조회
     */
    @GetMapping
    public List<CartItemResponse> getCart() {
        return cartService.getCartItems(mockUserId);
    }

    /**
     * ✅ 장바구니 항목 수정
     */
    @PatchMapping("/items/{cartItemId}")
    public CartItemResponse updateCartItem(@PathVariable Long cartItemId,
                                           @RequestBody @Valid CartItemRequest request) {
        return cartService.updateCartItem(cartItemId, request);
    }

    /**
     * ✅ 장바구니 항목 삭제
     */
    @DeleteMapping("/items/{cartItemId}")
    public void removeItem(@PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
    }

    /**
     * ✅ 장바구니 전체 비우기
     */
    @DeleteMapping
    public void clearCart() {
        cartService.clearCart(mockUserId);
    }
}
