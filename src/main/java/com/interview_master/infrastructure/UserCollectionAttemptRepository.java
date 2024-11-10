package com.interview_master.infrastructure;

import com.interview_master.domain.usercollectionattempt.UserCollectionAttempt;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface UserCollectionAttemptRepository extends Repository<UserCollectionAttempt, Long> {

  Optional<UserCollectionAttempt> findById(Long id);

  UserCollectionAttempt save(UserCollectionAttempt userCollectionAttempt);

  // 가장 최신 시도 기록 가져오기
  Optional<UserCollectionAttempt> findFirstByCollectionIdAndUserIdOrderByStartedAtDesc(Long collectionId, Long userId);

  void deleteByIdAndUserId(Long id, Long userId);

  int deleteAllByUserIdIn(List<Long> userIds);

  int deleteAllByCollectionIdIn(List<Long> collectionIds);

  List<UserCollectionAttempt> findByUserId(Long userId);
}
