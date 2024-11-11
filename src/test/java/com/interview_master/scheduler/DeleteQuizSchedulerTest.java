package com.interview_master.scheduler;

import static org.assertj.core.api.Assertions.assertThat;

import com.interview_master.domain.Access;
import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.domain.user.User;
import com.interview_master.domain.usercollectionattempt.UserCollectionAttempt;
import com.interview_master.domain.userquizattempt.UserQuizAttempt;
import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.QuizRepository;
import com.interview_master.infrastructure.UserCollectionAttemptRepository;
import com.interview_master.infrastructure.UserQuizAttemptRepository;
import com.interview_master.infrastructure.UserRepository;
import com.interview_master.login.OAuthProvider;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DeleteQuizSchedulerTest {

  @Autowired
  private DeleteQuizScheduler deleteQuizScheduler;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CollectionRepository collectionRepository;

  @Autowired
  private QuizRepository quizRepository;

  @Autowired
  private UserCollectionAttemptRepository userCollectionAttemptRepository;

  @Autowired
  private UserQuizAttemptRepository userQuizAttemptRepository;

  @Test
  void scheduleQuizDataCleanupTest() {
    // given
    // 1. 탈퇴 유저 생성
    User deletedUser = User.builder()
        .nickname("탈퇴예정유저_퀴즈삭제스케줄러")
        .email("deleted@test.com")
        .imgUrl("http://test.com/img.jpg")
        .oAuthProvider(OAuthProvider.NAVER)
        .build();
    deletedUser.markDeleted();
    userRepository.save(deletedUser);

    // 2. Collection 생성
    Collection collection = Collection.builder()
        .name("테스트 컬렉션")
        .description("테스트용 컬렉션입니다")
        .imgUrl("http://test.com/collection.jpg")
        .likes(0)
        .creator(deletedUser)
        .access(Access.PUBLIC)
        .build();
    collection.markDeleted(); //테스트 데이터니깐 나중에 컬렉션삭제 스케줄러를 통해 삭제될 수 있도록 soft delete 처리
    collectionRepository.save(collection);

    // 3. Quiz 생성
    Quiz quiz = Quiz.builder()
        .question("테스트 질문입니다")
        .answer("테스트 답변입니다")
        .collection(collection)
        .creator(deletedUser)
        .access(Access.PUBLIC)
        .build();
    quiz.markDeleted();
    quizRepository.save(quiz);

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
    deleteQuizScheduler.scheduleQuizDataCleanup();

    // then
    // 퀴즈와 퀴즈 시도 기록 삭제되었는지 확인
    assertThat(quizRepository.deleteAllByIdIn(List.of(quiz.getId()))).isEqualTo(0);
    assertThat(userQuizAttemptRepository.deleteAllByQuizIdIn(
        List.of(quiz.getId()))).isEqualTo(0);
  }
}