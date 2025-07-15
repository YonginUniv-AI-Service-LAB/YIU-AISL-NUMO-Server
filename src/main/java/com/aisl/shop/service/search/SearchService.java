package com.aisl.shop.service.search;

import com.aisl.shop.dto.request.search.SearchRequest;
import com.aisl.shop.dto.response.search.SearchResponse;
import com.aisl.shop.entity.Search;
import com.aisl.shop.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    public void save(SearchRequest dto) {
        // JSR-380에서 이미 유효성 검사를 했지만, 추가 방어 로직을 원할 경우 아래 사용
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

    public List<SearchResponse> getRecentSearches(Long userId) {
        return searchRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(s -> new SearchResponse(s.getKeyword()))
                .toList();
    }
}
