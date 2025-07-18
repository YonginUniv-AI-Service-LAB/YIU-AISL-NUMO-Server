package com.aisl.shop.repository;

import com.aisl.shop.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SearchRepository extends JpaRepository<Search, Long> {

    // 🔍 동일 키워드 중복 방지
    Optional<Search> findByUserIdAndKeyword(Long userId, String keyword);

    // 🔍 최근 검색어 10개 조회
    List<Search> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    // 🗑️ 사용자 전체 검색어 삭제
    void deleteAllByUserId(Long userId);
}
