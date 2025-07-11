package com.aisl.shop.repository;

import com.aisl.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId);
    void deleteByUserId(Long userId);

    Optional<CartItem> findByUserIdAndProductIdAndColorAndSize(
            Long userId, Long productId, String color, String size
    );
}
