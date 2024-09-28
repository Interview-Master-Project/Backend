package com.interview_master.service;

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
    private final CategoryService categoryService;

    // 로그인한 경우
    public List<CollectionSearchResult> searchCollectionForAuthenticatedUser(List<Long> categoryIds, List<String> keywords, Integer maxCorrectRate, Long userId) {
        // categoryIds 있으면 검증
        categoryService.areAllCategoriesExist(categoryIds);

        // queryDSL로 동적 쿼리

        return new ArrayList<>();
    }

    // 비로그인인 경우
    public List<CollectionSearchResult> searchCollectionForAnonymousUser(List<Integer> categoryIds, List<String> keywords, Integer maxCorrectRate) {
        return new ArrayList<>();
    }
}
