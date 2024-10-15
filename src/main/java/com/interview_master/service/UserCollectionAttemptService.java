package com.interview_master.service;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.user.User;
import com.interview_master.domain.usercollectionattempt.UserCollectionAttempt;
import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.UserCollectionAttemptRepository;
import com.interview_master.infrastructure.UserQuizAttemptRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCollectionAttemptService {

  private final UserCollectionAttemptRepository userCollectionAttemptRepository;
  private final UserQuizAttemptRepository userQuizAttemptRepository;

  private final CollectionRepository collectionRepository;
  private final EntityManager em;


  @Transactional
  public UserCollectionAttempt startSolveCollection(Long collectionId, Long userId) {
    // 컬렉션 존재 여부 검증
    Collection collection = collectionRepository.findByIdAndIsDeletedFalse(collectionId)
        .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));

    UserCollectionAttempt uca = UserCollectionAttempt.builder()
        .user(em.getReference(User.class, userId))
        .collection(collection)
        .totalQuizCount(collection.getQuizzes().size())
        .startedAt(LocalDateTime.now())
        .build();

    return userCollectionAttemptRepository.save(uca);
  }

  @Transactional
  public UserCollectionAttempt finishSolveCollection(Long ucaId, Long userId) {
    // uca 존재 여부 검증
    UserCollectionAttempt uca = userCollectionAttemptRepository.findById(ucaId)
        .orElseThrow(() -> new ApiException(ErrorCode.USER_COLLECTION_ATTEMPT_NOT_FOUND));

    // 요청한 사용자와 컬렉션 시도한 사용자가 일치하는지 검증
    uca.validateAttemptedByUser(userId);

    // 맞은 개수 가져오기
    int correct = userQuizAttemptRepository.countCorrectAttempts(ucaId, userId);

    // 퀴즈 종료하기(completedAt에 현재 시간 넣고 correctQuizCounts에 맞은 개수 넣기)
    uca.finishSolve(correct);

    return uca;
  }

}
