package com.aisl.shop.service.admin;

import com.aisl.shop.dto.response.banner.BannerResponse;
import com.aisl.shop.entity.Banner;
import com.aisl.shop.exception.admin.BannerNotFoundException;
import com.aisl.shop.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminBannerService {

    private final BannerRepository bannerRepository;

    /**
     * ✅ 프론트에서 넘긴 이미지 URL 리스트 저장
     */
    public void saveAll(List<String> imageUrls) {
        List<Banner> banners = imageUrls.stream()
                .map(url -> Banner.builder()
                        .imageUrl(url)
                        .build())
                .collect(Collectors.toList());

        bannerRepository.saveAll(banners);
    }

    /**
     * ✅ 배너 전체 조회
     */
    @Transactional(readOnly = true)
    public List<BannerResponse> getAllBanners() {
        return bannerRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * ✅ 배너 삭제
     */
    public void deleteBanner(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new BannerNotFoundException(id));
        bannerRepository.delete(banner);
    }

    /**
     * ✅ Entity → Response DTO 변환
     */
    private BannerResponse toDto(Banner banner) {
        return BannerResponse.builder()
                .id(banner.getId())
                .imageUrl(banner.getImageUrl())
                .createdAt(banner.getCreatedAt())
                .build();
    }
}
