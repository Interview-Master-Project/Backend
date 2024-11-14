package com.interview_master.kafka.consumer;

import static com.interview_master.common.constant.Constant.QUIZ_DELETE_TOPIC;
import static com.interview_master.common.constant.Constant.QUIZ_GROUP_ID;

import com.interview_master.domain.userquizattempt.UserQuizAttempt;
import com.interview_master.infrastructure.UserCollectionAttemptRepository;
import com.interview_master.infrastructure.UserQuizAttemptRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuizDeleteConsumer {

  private final UserQuizAttemptRepository uqaRepository;
  private final UserCollectionAttemptRepository ucaRepository;

  @KafkaListener(topics = QUIZ_DELETE_TOPIC, groupId = QUIZ_GROUP_ID, containerFactory = "defaultKafkaListenerContainerFactory")
  @Transactional
  public void quizDelete(@Payload Long dQuizId) {
    log.info("========== [Consumer] 퀴즈 삭제 비동기 처리 메시지 수행 ==========");

    log.info("========== 퀴즈 삭제로 인한 userCollectionAttempt 모두 수정 ==========");

    List<UserQuizAttempt> attemptsByQuizId = uqaRepository.findAllByQuizId(dQuizId);
    attemptsByQuizId.forEach(
        uqa -> ucaRepository.updateTotalCountAndCorrectCount(uqa.getCollectionAttempt().getId(),
            uqa.getIsCorrect() ? 1 : 0));
    log.info("========== 컬렉션 삭제 비동기 처리 완료 ==========");
  }

}
