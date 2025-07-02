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
