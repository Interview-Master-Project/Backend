package com.interview_master.service;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.user.User;
import com.interview_master.domain.usercollectionattempt.UserCollectionAttempt;
import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.UserCollectionAttemptRepository;
import com.interview_master.infrastructure.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCollectionAttemptService {

  private final UserCollectionAttemptRepository userCollectionAttemptRepository;
  private final UserRepository userRepository;
  private final CollectionRepository collectionRepository;

  @Transactional
  public UserCollectionAttempt startSolveCollection(Long collectionId, Long userId) {
    User user = userRepository.findByIdAndIsDeletedFalse(userId)
        .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

    Collection collection = collectionRepository.findByIdAndIsDeletedFalse(collectionId)
        .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));

    UserCollectionAttempt uca = UserCollectionAttempt.builder()
        .user(user)
        .collection(collection)
        .startedAt(LocalDateTime.now())
        .build();

    return userCollectionAttemptRepository.save(uca);
  }

}
