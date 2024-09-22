package com.interview_master.application;

import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.ui.response.CollectionSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchCollectionService {

    private final CollectionRepository collectionRepository;

    // 로그인한 경우
    public List<CollectionSearchResult> searchCollection(List<Long> categoryIds, List<String> keywords, Integer maxCorrectRate, Long userId) {
        // 카테고리 id들 검증
        if (categoryIds == null || categoryIds.isEmpty()) {}
        // 검색

        return new ArrayList<>();
    }

    // 비로그인인 경우
    public List<CollectionSearchResult> searchCollection(List<Integer> categoryIds, List<String> keywords, Integer maxCorrectRate) {
        return new ArrayList<>();
    }
}
