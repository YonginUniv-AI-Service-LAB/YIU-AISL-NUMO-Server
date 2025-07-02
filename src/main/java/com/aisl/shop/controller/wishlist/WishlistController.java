package com.aisl.shop.controller.wishlist;

import com.aisl.shop.dto.request.wishlist.WishlistRequestDto;
import com.aisl.shop.dto.response.wishlist.WishlistResponseDto;
import com.aisl.shop.service.wishlist.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    // 임시로 userId를 파라미터로 받음. 실제로는 인증된 사용자로부터 가져와야 함.
    @PostMapping
    public void addWishlist(@RequestParam Long userId, @RequestBody WishlistRequestDto request) {
        wishlistService.addWishlist(userId, request);
    }

    @GetMapping
    public List<WishlistResponseDto> getWishlist(@RequestParam Long userId) {
        return wishlistService.getWishlists(userId);
    }

    @DeleteMapping("/{productId}")
    public void deleteWishlist(@RequestParam Long userId, @PathVariable Long productId) {
        wishlistService.deleteWishlist(userId, productId);
    }
}
