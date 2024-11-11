package com.interview_master.scheduler;

import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeleteQuizScheduler {

  private final QuizRepository quizRepository;
  private final CollectionRepository collectionRepository;

  // 매일 새벽 4시에 soft delete된 퀴즈들과 퀴즈 시도 기록 삭제
  @Scheduled(cron = "0 30 3 * * *", zone = "Asia/Seoul")
  @Transactional
  public void scheduleQuizDataCleanup() {
    log.info("==================== 퀴즈 삭제 스케줄링 시작 ====================");


    int total = 0;
    log.info("==================== 총 {} 건 삭제 완료", total);
    log.info("==================== 컬렉션 삭제 스케줄링 완료 ====================");
  }

}
