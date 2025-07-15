package com.aisl.shop.service.wishlist;

import com.aisl.shop.dto.request.wishlist.WishlistRequestDto;
import com.aisl.shop.dto.response.wishlist.WishlistResponseDto;
import com.aisl.shop.entity.Wishlist;
import com.aisl.shop.exception.wishlist.WishlistAlreadyExistsException;
import com.aisl.shop.exception.wishlist.WishlistNotFoundException;
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

    /**
     * 찜 추가
     */
    @Transactional
    public void addWishlist(Long userId, WishlistRequestDto request) {
        boolean exists = wishlistRepository
                .existsByUserIdAndProductId(userId, request.getProductId());

        if (exists) {
            throw new WishlistAlreadyExistsException("이미 찜한 상품입니다.");
        }

        Wishlist wishlist = Wishlist.builder()
                .userId(userId)
                .productId(request.getProductId())
                .createdAt(LocalDateTime.now())
                .build();

        wishlistRepository.save(wishlist);
    }

    /**
     * 찜 목록 조회
     */
    public List<WishlistResponseDto> getWishlists(Long userId) {
        return wishlistRepository.findByUserId(userId).stream()
                .map(w -> WishlistResponseDto.builder()
                        .id(w.getId())
                        .productId(w.getProductId())
                        .createdAt(w.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 찜 삭제
     */
    @Transactional
    public void deleteWishlist(Long userId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new WishlistNotFoundException("찜 내역이 존재하지 않습니다."));

        wishlistRepository.delete(wishlist);
    }
}
