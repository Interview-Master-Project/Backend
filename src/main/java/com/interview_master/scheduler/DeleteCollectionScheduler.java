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
public class DeleteCollectionScheduler {

  private final UserRepository userRepository;
  private final UserCollectionAttemptRepository userCollectionAttemptRepository;
  private final QuizRepository quizRepository;
  private final CollectionsLikesRepository collectionsLikesRepository;
  private final CollectionRepository collectionRepository;

  // 매일 새벽 3시 10분에 soft delete된 컬렉션들과 하위 퀴즈들 hard delete 진행
//  @Scheduled(cron = "0 10 3 * * *", zone = "Asia/Seoul")
  @Scheduled(fixedRate = 60000)  // 60000ms = 1분
  @Transactional
  public void scheduleCollectionDataCleanup() {
    log.info("==================== 컬렉션 삭제 스케줄링 시작 ====================");

    // 1. 삭제할 컬렉션 id 리스트 가져오기
    List<Long> deleteCollectionIds = collectionRepository.findIdsByIsDeletedTrue();
    log.info("==================== 삭제 대상 컬렉 ID 목록 : {}", deleteCollectionIds);

    // 삭제할 컬렉션 없으면 종료
    if (deleteCollectionIds.isEmpty()) {
      log.info("==================== 삭제할 컬렉션이 없습니다 ====================");
      return;
    }

    // 2. quiz들 삭제
    int deletedQuizzes = quizRepository.deleteAllByCollectionIdIn(deleteCollectionIds);
    log.info("==================== 삭제할 컬렉션에 연관된 Quiz 삭제 완료 : {} 건", deletedQuizzes);

    // 4. 좋아요 기록 삭제
    int deletedLikes = collectionsLikesRepository.deleteAllByCollectionIdIn(deleteCollectionIds);
    log.info("==================== 삭제할 컬렉션에 연관된 좋아요 기록 삭제 완료 : {} 건", deletedLikes);

    // 5. 컬렉션 삭제
    int deletedCollections = collectionRepository.deleteAllByIdIn(deleteCollectionIds);
    log.info("==================== Collection 기록 삭제 완료 : {} 건", deletedCollections);

    int total = deletedCollections + deletedLikes + deletedQuizzes;
    log.info("==================== 총 {} 건 삭제 완료", total);
    log.info("==================== 컬렉션 삭제 스케줄링 완료 ====================");
  }

}
