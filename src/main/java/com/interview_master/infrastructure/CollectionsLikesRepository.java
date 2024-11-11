package com.interview_master.infrastructure;

import com.interview_master.domain.collectionlike.CollectionsLikes;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionsLikesRepository extends JpaRepository<CollectionsLikes, Long> {

  boolean existsByCollectionIdAndUserId(Long collectionId, Long userId);

  Optional<CollectionsLikes> findByCollectionIdAndUserId(Long collectionId, Long userId);

  /**
   * user들이 생성한 좋아요 기록 모두 삭제
   */
  int deleteAllByUserIdIn(List<Long> userIds);

  /**
   * collection에 관련된 좋아요 기록 모두 삭제
   */
  int deleteAllByCollectionIdIn(List<Long> collectionIds);

  // deleteUserScheduler 테스트 용 쿼리
  List<CollectionsLikes> findByUserId(Long userId);
}