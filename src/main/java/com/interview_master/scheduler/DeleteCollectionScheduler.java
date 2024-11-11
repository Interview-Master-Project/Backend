package com.interview_master.scheduler;

import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.CollectionsLikesRepository;
import com.interview_master.infrastructure.QuizRepository;
import com.interview_master.infrastructure.UserCollectionAttemptRepository;
import com.interview_master.infrastructure.UserQuizAttemptRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeleteCollectionScheduler {

  private final UserCollectionAttemptRepository userCollectionAttemptRepository;
  private final UserQuizAttemptRepository userQuizAttemptRepository;
  private final QuizRepository quizRepository;
  private final CollectionsLikesRepository collectionsLikesRepository;
  private final CollectionRepository collectionRepository;

  // 매일 새벽 3시 30분에 soft delete된 컬렉션들과 연관된 데이터들 모두 삭제
  @Scheduled(cron = "0 30 3 * * *", zone = "Asia/Seoul")
  @Transactional
  public void scheduleCollectionDataCleanup() {
    log.info("==================== 컬렉션 삭제 스케줄링 시작 ====================");

    // 삭제할 컬렉션 id 리스트 가져오기
    List<Long> deleteCollectionIds = collectionRepository.findIdsByIsDeletedTrue();
    log.info("==================== 삭제 대상 컬렉 ID 목록 : {}", deleteCollectionIds);

    // 삭제할 컬렉션 없으면 종료
    if (deleteCollectionIds.isEmpty()) {
      log.info("==================== 삭제할 컬렉션이 없습니다 ====================");
      return;
    }

    // 컬렉션에 해당하는 퀴즈 기록들 모두 삭제
    int deletedQuizAttempts = userQuizAttemptRepository.deleteAllByCollectionIdIn(
        deleteCollectionIds);
    log.info("==================== 컬렉션에 연관된 Quiz Attempt 삭제 완료 : {} 건", deletedQuizAttempts);

    // 컬렉션에 연관된 모든 시도 기록 삭제
    int deletedCollectionAttempts = userCollectionAttemptRepository.deleteAllByCollectionIdIn(
        deleteCollectionIds);
    log.info("==================== 컬렉션에 연관된 Collection Attempt 삭제 완료 : {} 건",
        deletedCollectionAttempts);

    // quiz들 삭제
    int deletedQuizzes = quizRepository.deleteAllByCollectionIdIn(deleteCollectionIds);
    log.info("==================== 컬렉션에 연관된 Quiz 삭제 완료 : {} 건", deletedQuizzes);

    // 좋아요 기록 삭제
    int deletedLikes = collectionsLikesRepository.deleteAllByCollectionIdIn(deleteCollectionIds);
    log.info("==================== 삭제할 컬렉션에 연관된 좋아요 기록 삭제 완료 : {} 건", deletedLikes);

    // 컬렉션 삭제
    int deletedCollections = collectionRepository.deleteAllByIdIn(deleteCollectionIds);
    log.info("==================== Collection 기록 삭제 완료 : {} 건", deletedCollections);

    int total = deletedCollections + deletedLikes + deletedQuizzes + deletedCollectionAttempts
        + deletedQuizAttempts;
    log.info("==================== 총 {} 건 삭제 완료", total);
    log.info("==================== 컬렉션 삭제 스케줄링 완료 ====================");
  }

}
