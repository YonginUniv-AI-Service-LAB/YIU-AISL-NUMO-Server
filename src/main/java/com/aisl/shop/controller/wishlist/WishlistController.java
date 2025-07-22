package com.aisl.shop.controller.wishlist;

import com.aisl.shop.dto.request.wishlist.WishlistRequestDto;
import com.aisl.shop.dto.response.wishlist.WishlistResponseDto;
import com.aisl.shop.service.wishlist.WishlistService;
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
            @RequestParam Long userId,
            @Valid @RequestBody WishlistRequestDto request
    ) {
        wishlistService.addWishlist(userId, request);
        return ResponseEntity.ok().build(); // 200 OK
    }

    // 📌 찜 목록 조회
    @GetMapping
    public ResponseEntity<List<WishlistResponseDto>> getWishlist(
            @RequestParam Long userId
    ) {
        List<WishlistResponseDto> wishlist = wishlistService.getWishlists(userId);
        return ResponseEntity.ok(wishlist);
    }

    // 📌 찜 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteWishlist(
            @RequestParam Long userId,
            @PathVariable Long productId
    ) {
        wishlistService.deleteWishlist(userId, productId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
