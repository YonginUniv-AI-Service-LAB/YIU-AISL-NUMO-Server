package com.aisl.shop.service.search;

import com.aisl.shop.dto.request.search.SearchRequest;
import com.aisl.shop.dto.response.search.SearchResponse;
import com.aisl.shop.entity.Search;
import com.aisl.shop.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional  // 트랜잭션 처리 추가
public class SearchService {

    private final SearchRepository searchRepository;

    // 🔹 검색어 저장
    public void save(SearchRequest dto) {
        if (dto.getUserId() == null || dto.getKeyword() == null || dto.getKeyword().trim().isEmpty()) {
            throw new IllegalArgumentException("userId 또는 keyword가 비어있습니다.");
        }

        searchRepository.findByUserIdAndKeyword(dto.getUserId(), dto.getKeyword())
                .ifPresentOrElse(
                        exist -> {
                            exist.setCreatedAt(LocalDateTime.now());
                            searchRepository.save(exist);
                        },
                        () -> {
                            Search newSearch = Search.builder()
                                    .userId(dto.getUserId())
                                    .keyword(dto.getKeyword())
                                    .createdAt(LocalDateTime.now())
                                    .build();
                            searchRepository.save(newSearch);
                        }
                );
    }

    // 🔹 최근 검색어 조회
    public List<SearchResponse> getRecentSearches(Long userId) {
        return searchRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(s -> new SearchResponse(s.getKeyword()))
                .toList();
    }

    // 🔹 단일 검색어 삭제
    public void delete(Long searchId) {
        searchRepository.deleteById(searchId);
    }

    // 🔹 해당 사용자 검색어 전체 삭제
    public void deleteAllByUserId(Long userId) {
        searchRepository.deleteAllByUserId(userId);
    }
}
