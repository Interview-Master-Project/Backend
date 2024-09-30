package com.interview_master.service;

import com.interview_master.dto.CollectionWithAttempts;
import com.interview_master.dto.DataPage;
import com.interview_master.dto.SortOrder;
import com.interview_master.infrastructure.CollectionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchCollectionService {

  private final CollectionRepository collectionRepository;
  private final CategoryService categoryService;

  // 로그인한 경우
  public Page<CollectionWithAttempts> searchCollections(
      List<Long> categoryIds, List<String> keywords, Integer maxCorrectRate, DataPage paging,
      SortOrder sortOrder, Long userId) {
    // categoryIds 있으면 검증
    categoryService.areAllCategoriesExist(categoryIds);

    // queryDSL로 동적 쿼리
    // categoryIds에 속하는 컬렉션 & 컬렉션의 이름과 설명에서 keywords 포함 & userCollectionAttempts에서 시도한 결과들의 최종 정답률이 maxCorrectRate 이하인 것
    Sort sort = createSort(sortOrder);
    Pageable pageable = createPageable(paging, sort);

    return collectionRepository.searchCollections(
        categoryIds, keywords, maxCorrectRate, pageable, userId);
  }

  private Sort createSort(SortOrder sortOrder) {
    return switch (sortOrder) {
      case LATEST -> Sort.by(Sort.Direction.DESC, "createdAt");
      case LOWEST_ACCURACY -> Sort.by(Sort.Direction.ASC, "accuracy");
    };
  }

  private Pageable createPageable(DataPage dataPage, Sort sort) {
    int pageNumber = dataPage.getOffset() / dataPage.getPageSize();
    return PageRequest.of(pageNumber, dataPage.getPageSize(), sort);
  }
}
