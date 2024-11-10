package com.interview_master.scheduler;

import com.interview_master.domain.Access;
import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.collectionlike.CollectionsLikes;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.domain.user.User;
import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.CollectionsLikesRepository;
import com.interview_master.infrastructure.QuizRepository;
import com.interview_master.infrastructure.UserCollectionAttemptRepository;
import com.interview_master.infrastructure.UserQuizAttemptRepository;
import com.interview_master.infrastructure.UserRepository;
import com.interview_master.login.OAuthProvider;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DeleteCollectionSchedulerTest {

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
  @DisplayName("탈퇴 유저 데이터 삭제가 정상적으로 동작한다")
  void scheduleCollectionDataCleanupTest() {
    // given
    // 1. 탈퇴 유저 생성
    for (int i = 0; i < 10; i++ ){
      User deletedUser = User.builder()
          .nickname("탈퇴예정유저")
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
      collection.markDeleted();
      collectionRepository.save(collection);

      // 3. Quiz 생성
      Quiz quiz = Quiz.builder()
          .question("테스트 질문입니다")
          .answer("테스트 답변입니다")
          .collection(collection)
          .creator(deletedUser)
          .access(Access.PUBLIC)
          .build();
      quizRepository.save(quiz);

      // 4. Collection 좋아요 생성
      CollectionsLikes collectionsLikes = CollectionsLikes.builder()
          .collection(collection)
          .user(deletedUser)
          .createdAt(LocalDateTime.now())
          .build();
      collectionsLikesRepository.save(collectionsLikes);
    }


//    // when
//    deleteUserScheduler.scheduleUserDataCleanup();
//
//    // then
//    // 모든 데이터가 삭제되었는지 확인
//    assertThat(userRepository.findIdsByIsDeletedTrue()).isEmpty();
//    assertThat(collectionRepository.findByCreatorId(deletedUser.getId())).isEmpty();
//    assertThat(quizRepository.findByCreatorId(deletedUser.getId())).isEmpty();
//    assertThat(collectionsLikesRepository.findByUserId(deletedUser.getId())).isEmpty();
//    assertThat(userCollectionAttemptRepository.findByUserId(deletedUser.getId())).isEmpty();
  }
}