package com.interview_master.infrastructure;

import com.interview_master.domain.usercollectionattempt.UserCollectionAttempt;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface UserCollectionAttemptRepository extends Repository<UserCollectionAttempt, Long> {

  Optional<UserCollectionAttempt> findById(Long id);

  UserCollectionAttempt save(UserCollectionAttempt userCollectionAttempt);

  // 가장 최신 시도 기록 가져오기
  Optional<UserCollectionAttempt> findFirstByCollectionIdAndUserIdOrderByStartedAtDesc(Long collectionId, Long userId);

  void deleteByIdAndUserId(Long id, Long userId);

  int deleteAllByUserIdIn(List<Long> userIds);

  int deleteAllByCollectionIdIn(List<Long> collectionIds);

  List<UserCollectionAttempt> findByUserId(Long userId);

  /**
   * 유저 탈퇴로 인한 시도 기록 익명 처리
   */
  @Modifying
  @Query("update UserCollectionAttempt ca set ca.user.id = 0L where ca.user.id in :userIds")
  int anonymizedByUserIdIn(@Param("userIds") List<Long> userIds);

}
