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

    @PostMapping
    public ResponseEntity<Void> saveSearch(@Valid @RequestBody SearchRequest dto) {
        searchService.save(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<SearchResponse>> getSearchHistory(@RequestParam Long userId) {
        // searchService.getRecentSearches(userId) 가 List<Search> 를 반환한다고 가정
        List<SearchResponse> response = searchService.getRecentSearches(userId).stream()
                .map(s -> new SearchResponse(s.getId(), s.getKeyword()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{searchId}")
    public ResponseEntity<Void> deleteSearch(@PathVariable Long searchId) {
        searchService.delete(searchId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllSearches(@RequestParam Long userId) {
        searchService.deleteAllByUserId(userId);
        return ResponseEntity.ok().build();
    }
}