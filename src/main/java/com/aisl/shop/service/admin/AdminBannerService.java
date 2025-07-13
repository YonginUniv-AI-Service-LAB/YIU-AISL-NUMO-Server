package com.aisl.shop.service.admin;

import com.aisl.shop.dto.request.banner.BannerRequest;
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

    public BannerResponse createBanner(BannerRequest request) {
        Banner banner = Banner.builder()
                .title(request.getTitle())
                .imageUrl(request.getImageUrl())
                .linkUrl(request.getLinkUrl())
                .build();

        return toDto(bannerRepository.save(banner));
    }

    public BannerResponse updateBanner(Long id, BannerRequest request) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("배너를 찾을 수 없습니다."));
        banner.setTitle(request.getTitle());
        banner.setImageUrl(request.getImageUrl());
        banner.setLinkUrl(request.getLinkUrl());

        return toDto(bannerRepository.save(banner));
    }

    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }

    public List<BannerResponse> getAllBanners() {
        return bannerRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private BannerResponse toDto(Banner banner) {
        return BannerResponse.builder()
                .id(banner.getId())
                .title(banner.getTitle())
                .imageUrl(banner.getImageUrl())
                .linkUrl(banner.getLinkUrl())
                .createdAt(banner.getCreatedAt())
                .build();
    }
}
