package com.interview_master.scheduler;

import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.CollectionsLikesRepository;
import com.interview_master.infrastructure.QuizRepository;
import com.interview_master.infrastructure.UserCollectionAttemptRepository;
import com.interview_master.infrastructure.UserQuizAttemptRepository;
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
  private final UserQuizAttemptRepository userQuizAttemptRepository;
  private final QuizRepository quizRepository;
  private final CollectionsLikesRepository collectionsLikesRepository;
  private final CollectionRepository collectionRepository;

  // 매달 1일 새벽 4시에 탈퇴 유저와 연관된 데이터들 모두 삭제
  @Scheduled(cron = "0 0 4 1 * *", zone = "Asia/Seoul")
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

    // CollectionAttempt 기록들 모두 익명 처리
    int deletedCollectionAttempts = userCollectionAttemptRepository.anonymizedByUserIdIn(deleteUserIds);
    log.info("==================== Collection Attempt 익명 처리 완료 : {} 건", deletedCollectionAttempts);

    // QuizAttempt 기록들 모두 익명 처리
    int deletedQuizAttempts = userQuizAttemptRepository.anonymizedByUserIdIn(deleteUserIds);
    log.info("==================== Collection Attempt 익명 처리 완료 : {} 건", deletedQuizAttempts);

    // 좋아요 기록 삭제
    int deletedLikes = collectionsLikesRepository.deleteAllByUserIdIn(deleteUserIds);
    log.info("==================== 좋아요 기록 삭제 완료 : {} 건", deletedLikes);

    // 6. 유저 삭제
    int deletedUsers = userRepository.deleteByIdIn(deleteUserIds);
    log.info("==================== User 기록 삭제 완료 : {} 건", deletedUsers);

    int total = deletedUsers + deletedLikes + deletedCollectionAttempts + deletedQuizAttempts;
    log.info("==================== 총 {} 건 삭제 완료", total);
    log.info("==================== 탈퇴 유저 삭제 스케줄링 완료 ====================");
  }

}
