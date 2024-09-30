package com.interview_master.service;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.collection.Collection;
import com.interview_master.dto.DataPage;
import com.interview_master.infrastructure.CollectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    return collectionRepository.findByIdAndIsDeletedFalse(collectionId)
        .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));
  }

  /**
   * user의 컬렉션을 offset 기반으로 페이징 + 최신 수정 순으로
   */
  public Page<Collection> userCollections(Long userId, DataPage paging) {
    Pageable pageable = PageRequest.of(paging.getOffset() / paging.getPageSize(),
        paging.getPageSize(), Sort.by("updatedAt").descending());
    return collectionRepository.findByCreatorIdAndIsDeletedFalse(userId,
        pageable);
  }

  /**
   * user의 히스토리 반환
   */
  public Page<Collection> userAttemptedCollections(Long userId, DataPage paging) {
    Pageable pageable = PageRequest.of(paging.getOffset() / paging.getPageSize(),
        paging.getPageSize());
    return collectionRepository.findAttemptedCollectionsByUserOrderByLatestAttempt(
        userId, pageable);
  }
}
