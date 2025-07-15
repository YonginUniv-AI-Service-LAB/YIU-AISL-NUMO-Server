package com.aisl.shop.controller.search;

import com.aisl.shop.dto.request.search.SearchRequest;
import com.aisl.shop.dto.response.search.SearchResponse;
import com.aisl.shop.service.search.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    // 🔹 검색어 저장 (검색 시 자동 호출)
    @PostMapping
    public ResponseEntity<Void> saveSearch(@Valid @RequestBody SearchRequest dto) {
        searchService.save(dto);
        return ResponseEntity.ok().build();
    }

    // 🔹 최근 검색어 조회
    @GetMapping
    public ResponseEntity<List<SearchResponse>> getSearchHistory(@RequestParam Long userId) {
        List<SearchResponse> response = searchService.getRecentSearches(userId);
        return ResponseEntity.ok(response);
    }
}
