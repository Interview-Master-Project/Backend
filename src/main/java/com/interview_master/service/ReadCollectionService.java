package com.interview_master.service;

import static com.interview_master.util.PageSortUtils.createPageable;
import static com.interview_master.util.PageSortUtils.createSort;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.Access;
import com.interview_master.domain.collection.Collection;
import com.interview_master.dto.CollectionWithAttempt;
import com.interview_master.dto.DataPage;
import com.interview_master.dto.SortOrder;
import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.UserCollectionAttemptRepository;
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
  private final UserCollectionAttemptRepository userCollectionAttemptRepository;

  public Collection getCollectionById(Long collectionId) {
    return collectionRepository.findByIdWithQuizzes(collectionId)
        .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));
  }

  /**
   * user의 컬렉션을 offset 기반으로 페이징 + 정렬 (최신 생성 순 or 낮은 최근 정답률 순)
   */
  public Page<CollectionWithAttempt> userCollections(Long userId, DataPage paging,
      SortOrder sortOrder) {
    Sort sort = createSort(sortOrder);
    Pageable pageable = createPageable(paging, sort);

    return collectionRepository.myCollections(userId,
        pageable);
  }

  /**
   * user의 히스토리 반환
   */
  public Page<CollectionWithAttempt> getMyCollectionHistory(Long userId, DataPage paging,
      Access filter) {
    Pageable pageable = createPageable(paging);

    return collectionRepository.getHistory(userId,
        filter, pageable);
  }

  /**
   * 각 collection에 대해 유저의 최근 시도 정보, 누적 시도 정보, 페이징 정보 반환하는 메서드
   */
//  private CollectionWithAttemptsPaging getCollectionWithAttemptsPaging(
//      Page<CollectionWithLike> collections) {
//    List<UserCollectionAttempt> collectionAttempts = userCollectionAttemptRepository.findByCollectionIn(
//        collections.getContent());
//
//    // 컬렉션별 시도 매핑
//    Map<Long, List<UserCollectionAttempt>> attemptsByCollectionId = collectionAttempts.stream()
//        .collect(Collectors.groupingBy(attempt -> attempt.getCollection().getId()));
//
//    // CollectionWithAttempt 객체 생성
//    List<CollectionWithAttempt> collectionsWithAttempt = collections.getContent().stream()
//        .map(collection -> {
//          List<UserCollectionAttempt> attempts = attemptsByCollectionId.getOrDefault(
//              collection.getId(), Collections.emptyList());
//          int totalAttempts = attempts.stream().mapToInt(UserCollectionAttempt::getTotalQuizCount)
//              .sum();
//          int totalCorrectAttempts = attempts.stream()
//              .mapToInt(UserCollectionAttempt::getCorrectQuizCount).sum();
//
//
//          UserCollectionAttempt recentAttempt = attempts.stream()
//              .filter(attempt -> attempt.getCompletedAt() != null)
//              .max(Comparator.comparing(UserCollectionAttempt::getCompletedAt))
//              .orElse(null);
//
//          return CollectionWithAttempt.builder()
//              .collection(collection)
//              .totalAttempts(totalAttempts)
//              .totalCorrectAttempts(totalCorrectAttempts)
//              .recentAttempts(recentAttempt != null ? recentAttempt.getTotalQuizCount() : 0)
//              .recentCorrectAttempts(
//                  recentAttempt != null ? recentAttempt.getCorrectQuizCount() : 0)
//              .build();
//        })
//        .toList();
//
//    PageInfo pageInfo = new PageInfo(
//        collections.hasNext(),
//        collections.getNumber() + 1,
//        collections.getTotalPages()
//    );
//
//    // 6. CollectionWithAttemptsPaging 객체 생성 및 반환
//    return new CollectionWithAttemptsPaging(collectionsWithAttempt, pageInfo);
//  }

}