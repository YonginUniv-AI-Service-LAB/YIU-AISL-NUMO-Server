package com.aisl.shop.service.wishlist;

import com.aisl.shop.dto.request.wishlist.WishlistRequestDto;
import com.aisl.shop.dto.response.wishlist.WishlistResponseDto;
import com.aisl.shop.entity.Wishlist;
import com.aisl.shop.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    @Transactional
    public void addWishlist(Long userId, WishlistRequestDto request) {
        wishlistRepository.findByUserIdAndProductId(userId, request.getProductId())
                .ifPresent(w -> { throw new IllegalArgumentException("이미 찜한 상품입니다."); });

        Wishlist wishlist = Wishlist.builder()
                .userId(userId)
                .productId(request.getProductId())
                .createdAt(LocalDateTime.now())
                .build();

        wishlistRepository.save(wishlist);
    }

    public List<WishlistResponseDto> getWishlists(Long userId) {
        return wishlistRepository.findByUserId(userId).stream()
                .map(w -> WishlistResponseDto.builder()
                        .id(w.getId())
                        .productId(w.getProductId())
                        .createdAt(w.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteWishlist(Long userId, Long productId) {
        wishlistRepository.deleteByUserIdAndProductId(userId, productId);
    }
}
