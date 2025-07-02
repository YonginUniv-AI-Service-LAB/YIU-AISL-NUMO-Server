package com.aisl.shop.service.cartitem;

import com.aisl.shop.dto.request.cartitem.CartItemRequest;
import com.aisl.shop.dto.response.cartitem.CartItemResponse;
import com.aisl.shop.entity.CartItem;
import com.aisl.shop.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;

    public CartItemResponse addToCart(Long userId, CartItemRequest request) {
        CartItem item = new CartItem();
        item.setUserId(userId);
        item.setProductId(request.getProductId());
        item.setQuantity(request.getQuantity());

        return toDto(cartItemRepository.save(item));
    }

    public List<CartItemResponse> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .toList();
    }

    public CartItemResponse updateQuantity(Long cartItemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 항목을 찾을 수 없습니다."));
        item.setQuantity(quantity);
        return toDto(cartItemRepository.save(item));
    }

    public void removeItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    private CartItemResponse toDto(CartItem item) {
        return CartItemResponse.builder()
                .id(item.getId())
                .userId(item.getUserId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .createdAt(item.getCreatedAt())
                .build();
    }
}
