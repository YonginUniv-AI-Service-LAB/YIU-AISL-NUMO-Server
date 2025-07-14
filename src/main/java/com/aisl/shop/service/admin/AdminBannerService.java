package com.aisl.shop.service.admin;

import com.aisl.shop.dto.response.banner.BannerResponse;
import com.aisl.shop.entity.Banner;
import com.aisl.shop.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminBannerService {

    private final BannerRepository bannerRepository;

    // 프론트에서 넘긴 이미지 URL들 저장
    public void saveAll(List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            Banner banner = Banner.builder()
                    .imageUrl(imageUrl)
                    .build();
            bannerRepository.save(banner);
        }
    }

    // 배너 전체 조회
    public List<BannerResponse> getAllBanners() {
        return bannerRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 배너 삭제
    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }

    // Entity -> DTO 변환
    private BannerResponse toDto(Banner banner) {
        return BannerResponse.builder()
                .id(banner.getId())
                .imageUrl(banner.getImageUrl())
                .createdAt(banner.getCreatedAt())
                .build();
    }
}
