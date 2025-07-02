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

    @PostMapping("/cart")
    public CartItemResponse addToCart(@RequestBody CartItemRequest request) {
        return cartService.addToCart(mockUserId, request);
    }

    @GetMapping("/cart")
    public List<CartItemResponse> getCart() {
        return cartService.getCartItems(mockUserId);
    }

    @PatchMapping("/cart/items/{cartItemId}")
    public CartItemResponse updateQuantity(@PathVariable Long cartItemId,
                                           @RequestParam Integer quantity) {
        return cartService.updateQuantity(cartItemId, quantity);
    }

    @DeleteMapping("/cart/items/{cartItemId}")
    public void removeItem(@PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
    }

    @DeleteMapping("/cart")
    public void clearCart() {
        cartService.clearCart(mockUserId);
    }
}
