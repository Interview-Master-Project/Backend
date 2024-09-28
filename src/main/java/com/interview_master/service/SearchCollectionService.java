package com.interview_master.service;

import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.ui.response.CollectionWithAttemptsPaging;
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
    public List<CollectionWithAttemptsPaging> searchCollectionForAuthenticatedUser(List<Long> categoryIds, List<String> keywords, Integer maxCorrectRate, Long userId) {
        // categoryIds 있으면 검증
        categoryService.areAllCategoriesExist(categoryIds);

        // queryDSL로 동적 쿼리
            // categoryIds에 속하는 컬렉션 & 컬렉션의 이름과 설명에서 keywords 포함 & userCollectionAttempts에서 시도한 결과들의 최종 정답률이 maxCorrectRate 이하인 것

        return new ArrayList<>();
    }

    // 비로그인인 경우
    public List<CollectionWithAttemptsPaging> searchCollectionForAnonymousUser(List<Integer> categoryIds, List<String> keywords, Integer maxCorrectRate) {
        return new ArrayList<>();
    }
}
