package com.aisl.shop.service.cartitem;

import com.aisl.shop.dto.request.cartitem.CartItemRequest;
import com.aisl.shop.dto.response.cartitem.CartItemResponse;
import com.aisl.shop.entity.CartItem;
import com.aisl.shop.entity.Product;
import com.aisl.shop.exception.cartitem.CartItemNotFoundException;
import com.aisl.shop.exception.cartitem.InvalidCartItemQuantityException;
import com.aisl.shop.exception.product.ProductNotFoundException;
import com.aisl.shop.repository.CartItemRepository;
import com.aisl.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartitemService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    /**
     * ✅ 장바구니에 상품 추가 (색상+사이즈 기준 중복 체크 + 수량 합산)
     */
    public CartItemResponse addToCart(Long userId, CartItemRequest request) {
        if (request.getQuantity() < 1) {
            throw new InvalidCartItemQuantityException(request.getQuantity());
        }

        // 상품 가격 조회
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));

        int unitPrice = request.getPrice();
        int totalPrice = unitPrice * request.getQuantity();

        return cartItemRepository.findByUserIdAndProductIdAndColorAndSize(
                userId,
                request.getProductId(),
                request.getColor(),
                request.getSize()
        ).map(existingItem -> {
            int newQuantity = existingItem.getQuantity() + request.getQuantity();
            existingItem.setQuantity(newQuantity);
            existingItem.setUnitPrice(unitPrice);
            existingItem.setTotalPrice(unitPrice * newQuantity);
            return toDto(cartItemRepository.save(existingItem));
        }).orElseGet(() -> {
            CartItem newItem = new CartItem();
            newItem.setUserId(userId);
            newItem.setProductId(request.getProductId());
            newItem.setBrandName(request.getBrandName());
            newItem.setProductName(request.getProductName());
            newItem.setColor(request.getColor());
            newItem.setSize(request.getSize());
            newItem.setQuantity(request.getQuantity());
            newItem.setUnitPrice(unitPrice);
            newItem.setTotalPrice(totalPrice);
            return toDto(cartItemRepository.save(newItem));
        });
    }

    /**
     * ✅ 장바구니 목록 조회
     */
    public List<CartItemResponse> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * ✅ 장바구니 항목 수정 (옵션 + 수량 + 이름 변경)
     */
    public CartItemResponse updateCartItem(Long cartItemId, CartItemRequest request) {
        if (request.getQuantity() < 1) {
            throw new InvalidCartItemQuantityException(request.getQuantity());
        }

        // 상품 가격 계산
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));

        int originalPrice = product.getPrice();
        int discountPrice = product.getDiscountPrice() != null ? product.getDiscountPrice() : 0;
        int unitPrice = originalPrice - discountPrice;
        int totalPrice = unitPrice * request.getQuantity();

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        item.setProductId(request.getProductId());
        item.setBrandName(request.getBrandName());
        item.setProductName(request.getProductName());
        item.setColor(request.getColor());
        item.setSize(request.getSize());
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(unitPrice);
        item.setTotalPrice(totalPrice);

        return toDto(cartItemRepository.save(item));
    }

    /**
     * ✅ 장바구니 항목 삭제
     */
    public void removeItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new CartItemNotFoundException(cartItemId);
        }
        cartItemRepository.deleteById(cartItemId);
    }

    /**
     * ✅ 장바구니 전체 비우기
     */
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    /**
     * ✅ 응답 DTO 변환
     */
    private CartItemResponse toDto(CartItem item) {
        return CartItemResponse.builder()
                .id(item.getId())
                .userId(item.getUserId())
                .productId(item.getProductId())
                .brandName(item.getBrandName())
                .productName(item.getProductName())
                .color(item.getColor())
                .size(item.getSize())
                .quantity(item.getQuantity())
                .unitprice(item.getUnitPrice())
                .totalPrice(item.getTotalPrice())
                .createdAt(item.getCreatedAt())
                .build();
    }
}






