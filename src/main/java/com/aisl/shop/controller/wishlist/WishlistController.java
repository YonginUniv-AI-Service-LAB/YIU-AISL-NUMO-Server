package com.aisl.shop.controller.wishlist;

import com.aisl.shop.dto.request.wishlist.WishlistRequest;
import com.aisl.shop.dto.response.wishlist.WishlistResponse;
import com.aisl.shop.service.wishlist.WishlistService;
import com.aisl.shop.util.SecurityUtil; // 유저 ID 추출 유틸
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    // 📌 찜 추가
    @PostMapping
    public ResponseEntity<Void> addWishlist(
            @Valid @RequestBody WishlistRequest request
    ) {
        Long userId = SecurityUtil.getCurrentUserId(); // ⬅️ 토큰에서 유저 ID 추출
        wishlistService.addWishlist(userId, request);
        return ResponseEntity.ok().build();
    }

    // 📌 찜 목록 조회
    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getWishlist() {
        Long userId = SecurityUtil.getCurrentUserId(); // ⬅️ 토큰에서 유저 ID 추출
        List<WishlistResponse> wishlist = wishlistService.getWishlists(userId);
        return ResponseEntity.ok(wishlist);
    }

    // 📌 찜 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteWishlist(
            @PathVariable Long productId
    ) {
        Long userId = SecurityUtil.getCurrentUserId(); // ⬅️ 토큰에서 유저 ID 추출
        wishlistService.deleteWishlist(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
