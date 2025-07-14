package com.aisl.shop.controller.admin;

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

    // 배너 이미지 URL 리스트 등록
    @PostMapping
    public ResponseEntity<Void> uploadBanners(@RequestBody List<String> imageUrls) {
        bannerService.saveAll(imageUrls);
        return ResponseEntity.ok().build();
    }

    // 배너 목록 조회
    @GetMapping
    public ResponseEntity<?> getAllBanners() {
        return ResponseEntity.ok(bannerService.getAllBanners());
    }

    // 배너 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.ok().build();
    }
}
