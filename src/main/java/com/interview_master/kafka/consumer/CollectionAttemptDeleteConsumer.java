package com.interview_master.kafka.consumer;

import static com.interview_master.common.constant.Constant.COLLECTION_ATTEMPT_DELETE_TOPIC;
import static com.interview_master.common.constant.Constant.COLLECTION_ATTEMPT_GROUP_ID;

import com.interview_master.infrastructure.UserCollectionAttemptRepository;
import com.interview_master.infrastructure.UserQuizAttemptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CollectionAttemptDeleteConsumer {

  private final UserQuizAttemptRepository uqaRepository;
  private final UserCollectionAttemptRepository ucaRepository;

  @KafkaListener(topics = COLLECTION_ATTEMPT_DELETE_TOPIC, groupId = COLLECTION_ATTEMPT_GROUP_ID, containerFactory = "defaultKafkaListenerContainerFactory")
  @Transactional
  public void collectionAttemptDelete(@Payload Long dCollectionAttemptId) {
    log.info("========== [Consumer] 컬렉션 시도 기록 삭제 비동기 처리 메시지 수행 ==========");

    int deletedQuizAttempts = uqaRepository.deleteAllByCollectionAttemptId(dCollectionAttemptId);
    log.info("========== Collection Attempt ID: {} 의 Quiz Attempt {} 개 Hard Delete 처리 완료",
        dCollectionAttemptId, deletedQuizAttempts);

    int deletedCollectionAttempt = ucaRepository.deleteById(dCollectionAttemptId);
    if (deletedCollectionAttempt == 1) {
      log.info("========== Collection Attempt ID: {} Hard Delete 처리 완료", dCollectionAttemptId);
    } else {
      log.info("========== Collection Attempt ID: {} Hard Delete 처리 에러 발생", dCollectionAttemptId);
    }

    log.info(
        "========== Collection Attempt ID: {} 와 이에 속한 Quiz Attempt {}, 총 {} 개의 데이터 hard Delete 처리 완료",
        dCollectionAttemptId, deletedQuizAttempts, deletedQuizAttempts + deletedCollectionAttempt);

    log.info("========== 최근 컬렉션 시도 삭제 비동기 처리 완료 ==========");
  }

}
