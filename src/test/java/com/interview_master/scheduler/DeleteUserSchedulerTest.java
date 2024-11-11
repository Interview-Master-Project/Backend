package com.interview_master.scheduler;

import static org.assertj.core.api.Assertions.assertThat;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.collectionlike.CollectionsLikes;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.domain.user.User;
import com.interview_master.domain.usercollectionattempt.UserCollectionAttempt;
import com.interview_master.domain.userquizattempt.UserQuizAttempt;
import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.CollectionsLikesRepository;
import com.interview_master.infrastructure.QuizRepository;
import com.interview_master.infrastructure.UserCollectionAttemptRepository;
import com.interview_master.infrastructure.UserQuizAttemptRepository;
import com.interview_master.infrastructure.UserRepository;
import com.interview_master.login.OAuthProvider;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DeleteUserSchedulerTest {

  @Autowired
  private DeleteUserScheduler deleteUserScheduler;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CollectionRepository collectionRepository;

  @Autowired
  private QuizRepository quizRepository;

  @Autowired
  private CollectionsLikesRepository collectionsLikesRepository;

  @Autowired
  private UserCollectionAttemptRepository userCollectionAttemptRepository;

  @Autowired
  private UserQuizAttemptRepository userQuizAttemptRepository;

  @Test
  void scheduleUserDataCleanupTest() {
    // given
    // 1. 탈퇴 유저 생성
    User deletedUser = User.builder()
        .nickname("탈퇴예정유저_탈퇴유저삭제스케줄러")
        .email("deleted@test.com")
        .imgUrl("http://test.com/img.jpg")
        .oAuthProvider(OAuthProvider.NAVER)
        .build();
    deletedUser.markDeleted();
    userRepository.save(deletedUser);

    // 2. Collection 조회
    Collection collection = collectionRepository.findByIdAndIsDeletedFalse(3L)
        .orElseThrow(() -> new ApiException(
            ErrorCode.COLLECTION_NOT_FOUND));

    // 3. Quiz 조회
    Quiz quiz = quizRepository.findByIdAndIsDeletedFalse(136L)
        .orElseThrow(() -> new ApiException(ErrorCode.QUIZ_NOT_FOUND));

    // 4. Collection 좋아요 생성
    CollectionsLikes collectionsLikes = CollectionsLikes.builder()
        .collection(collection)
        .user(deletedUser)
        .createdAt(LocalDateTime.now())
        .build();
    collectionsLikesRepository.save(collectionsLikes);

    // 5. Collection Attempt 생성
    UserCollectionAttempt collectionAttempt = UserCollectionAttempt.builder()
        .user(deletedUser)
        .collection(collection)
        .startedAt(LocalDateTime.now().minusHours(1))
        .completedAt(LocalDateTime.now())
        .totalQuizCount(1)
        .correctQuizCount(1)
        .build();
    userCollectionAttemptRepository.save(collectionAttempt);

    // 6. Quiz Attempt 생성
    UserQuizAttempt quizAttempt = UserQuizAttempt.builder()
        .collectionAttempt(collectionAttempt)
        .quiz(quiz)
        .user(deletedUser)
        .isCorrect(true)
        .answeredAt(LocalDateTime.now())
        .build();
    userQuizAttemptRepository.save(quizAttempt);

    // when
    deleteUserScheduler.scheduleUserDataCleanup();

    // then
    // 모든 데이터가 삭제되었는지 확인
    assertThat(userRepository.findIdsByIsDeletedTrue()).isEmpty();
    assertThat(collectionsLikesRepository.findByUserId(deletedUser.getId())).isEmpty();
    assertThat(userCollectionAttemptRepository.findByUserId(0L).size()).isEqualTo(2);
    assertThat(userQuizAttemptRepository.findByUserId(0L).size()).isEqualTo(2);
  }
}