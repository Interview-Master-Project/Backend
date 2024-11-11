package com.interview_master.scheduler;

import com.interview_master.infrastructure.QuizRepository;
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
public class DeleteQuizScheduler {

  private final QuizRepository quizRepository;
  private final UserQuizAttemptRepository userQuizAttemptRepository;

  // 매일 새벽 4시에 soft delete된 퀴즈들과 퀴즈 시도 기록 삭제
  @Scheduled(cron = "0 30 3 * * *", zone = "Asia/Seoul")
  @Transactional
  public void scheduleQuizDataCleanup() {
    log.info("==================== 퀴즈 삭제 스케줄링 시작 ====================");

    // 삭제될 퀴즈 id list 조회
    List<Long> deletedQuizIds = quizRepository.findIdsByIsDeletedTrue();
    if (deletedQuizIds.isEmpty()) {
      log.info("==================== 삭제할 퀴즈가 없습니다 ====================");
      return;
    }

    // 퀴즈 시도 기록 삭제
    int deletedQuizAttempts = userQuizAttemptRepository.deleteAllByQuizIdIn(deletedQuizIds);
    log.info("==================== 퀴즈에 연관된 Quiz Attempt 삭제 완료 : {} 건", deletedQuizAttempts);

    // 퀴즈 삭제
    int deletedQuizzes = quizRepository.deleteAllByIdIn(deletedQuizIds);
    log.info("==================== Quiz 삭제 완료 : {} 건", deletedQuizzes);

    int total = deletedQuizzes + deletedQuizAttempts;
    log.info("==================== 총 {} 건 삭제 완료", total);
    log.info("==================== 퀴즈 삭제 스케줄링 완료 ====================");
  }

}
