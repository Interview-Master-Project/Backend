package com.interview_master.service;

import static com.interview_master.util.PageSortUtils.createPageable;
import static com.interview_master.util.PageSortUtils.createSort;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.Access;
import com.interview_master.domain.collection.Collection;
import com.interview_master.dto.DataPage;
import com.interview_master.dto.SortOrder;
import com.interview_master.infrastructure.CollectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReadCollectionService {

  private final CollectionRepository collectionRepository;

  public Collection getCollectionById(Long collectionId) {
    return collectionRepository.findByIdWithQuizzes(collectionId)
        .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));
  }

  /**
   * user의 컬렉션을 offset 기반으로 페이징 + 정렬 (최신 생성 순 or 낮은 최근 정답률 순)
   */
  public Page<Collection> userCollections(Long userId, DataPage paging, SortOrder sortOrder) {
    Sort sort = createSort(sortOrder);
    Pageable pageable = createPageable(paging, sort);
    return collectionRepository.findByCreatorIdAndIsDeletedFalse(userId, pageable);
  }

  /**
   * user의 히스토리 반환
   */
  public Page<Collection> getMyCollectionHistory(Long userId, DataPage paging, Access filter) {
    Pageable pageable = createPageable(paging);
    return collectionRepository.findUserCollectionHistory(userId, filter, pageable);
  }
}
