package com.interview_master.scheduler;

import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.CollectionsLikesRepository;
import com.interview_master.infrastructure.QuizRepository;
import com.interview_master.infrastructure.UserCollectionAttemptRepository;
import com.interview_master.infrastructure.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeleteUserScheduler {

  private final UserRepository userRepository;
  private final UserCollectionAttemptRepository userCollectionAttemptRepository;
  private final QuizRepository quizRepository;
  private final CollectionsLikesRepository collectionsLikesRepository;
  private final CollectionRepository collectionRepository;

  // 매달 1일 새벽 3시에 탈퇴 유저와 연관된 데이터들 모두 삭제
  @Scheduled(cron = "0 0 3 1 * *", zone = "Asia/Seoul")
  @Transactional
  public void scheduleUserDataCleanup() {
    log.info("==================== 탈퇴 유저 삭제 스케줄링 시작 ====================");

    // 1. 삭제할 유저의 id 리스트 가져오기
    List<Long> deleteUserIds = userRepository.findIdsByIsDeletedTrue();
    log.info("==================== 삭제 대상 유저 ID 목록 : {}", deleteUserIds);

    // 삭제할 유저 없으면 종료
    if (deleteUserIds.isEmpty()) {
      log.info("==================== 삭제할 유저가 없습니다 ====================");
      return;
    }

    // 2. CollectionAttempt 삭제 (QuizAttempt도 자동 삭제)
    int deletedCollectionAttempts = userCollectionAttemptRepository.deleteAllByUserIdIn(deleteUserIds);
    log.info("==================== Collection Attempt 삭제 완료 : {} 건", deletedCollectionAttempts);

    // 3. quiz들 삭제
    int deletedQuizzes = quizRepository.deleteByCreatorIdIn(deleteUserIds);
    log.info("==================== Quiz 삭제 완료 : {} 건", deletedQuizzes);

    // 4. 좋아요 기록 삭제
    int deletedLikes = collectionsLikesRepository.deleteAllByUserIdIn(deleteUserIds);
    log.info("==================== 좋아요 기록 삭제 완료 : {} 건", deletedLikes);

    // 5. 컬렉션 삭제
    int deletedCollections = collectionRepository.deleteAllByCreatorIdIn(deleteUserIds);
    log.info("==================== Collection 기록 삭제 완료 : {} 건", deletedCollections);

    // 6. 유저 삭제
    int deletedUsers = userRepository.deleteByIdIn(deleteUserIds);
    log.info("==================== User 기록 삭제 완료 : {} 건", deletedUsers);

    int total = deletedUsers + deletedCollections + deletedLikes + deletedCollectionAttempts
        + deletedQuizzes;
    log.info("==================== 총 {} 건 삭제 완료", total);
    log.info("==================== 탈퇴 유저 삭제 스케줄링 완료 ====================");
  }

}
