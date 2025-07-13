package com.aisl.shop.controller.admin;

import com.aisl.shop.dto.request.banner.BannerRequest;
import com.aisl.shop.dto.response.banner.BannerResponse;
import com.aisl.shop.service.admin.AdminBannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/banners")
@RequiredArgsConstructor
public class AdminBannerController {

    private final AdminBannerService bannerService;

    // 배너 등록
    @PostMapping
    public ResponseEntity<BannerResponse> createBanner(@RequestBody BannerRequest request) {
        return ResponseEntity.ok(bannerService.createBanner(request));
    }

    // 배너 수정
    @PatchMapping("/{id}")
    public ResponseEntity<BannerResponse> updateBanner(@PathVariable Long id, @RequestBody BannerRequest request) {
        return ResponseEntity.ok(bannerService.updateBanner(id, request));
    }

    // 배너 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.ok().build();
    }

    // 배너 목록 조회 (for admin preview or front)
    @GetMapping
    public ResponseEntity<List<BannerResponse>> getAllBanners() {
        return ResponseEntity.ok(bannerService.getAllBanners());
    }
}
