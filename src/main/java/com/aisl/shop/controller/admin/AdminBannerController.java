package com.aisl.shop.controller.admin;

import com.aisl.shop.service.admin.AdminBannerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/banners")
@RequiredArgsConstructor
@Validated
public class AdminBannerController {

    private final AdminBannerService bannerService;

    // 배너 이미지 URL 리스트 등록
    @PostMapping
    public ResponseEntity<Void> uploadBanners(@RequestBody @Valid @NotEmpty(message = "배너 이미지 URL 리스트는 비어 있을 수 없습니다.") List<@NotEmpty(message = "배너 이미지 URL은 비어 있을 수 없습니다.") String> imageUrls) {
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
    public ResponseEntity<Void> deleteBanner(@PathVariable @NotNull(message = "배너 ID는 필수입니다.") Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.ok().build();
    }
}
