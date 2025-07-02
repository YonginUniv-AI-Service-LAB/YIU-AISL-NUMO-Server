package com.aisl.shop.repository;

import com.aisl.shop.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SearchRepository extends JpaRepository<Search, Long> {
    Optional<Search> findByUserIdAndKeyword(Long userId, String keyword);
    List<Search> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
