package com.interview_master.kafka.consumer;

import static com.interview_master.common.constant.Constant.COLLECTION_DELETE_TOPIC;
import static com.interview_master.common.constant.Constant.COLLECTION_GROUP_ID;

import com.interview_master.infrastructure.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CollectionDeleteConsumer {

  private final QuizRepository quizRepository;

  @KafkaListener(topics = COLLECTION_DELETE_TOPIC, groupId = COLLECTION_GROUP_ID, containerFactory = "defaultKafkaListenerContainerFactory")
  @Transactional
  public void collectionDelete(@Payload Long dCollectionId) {
    log.info("========== [Consumer] 컬렉션 삭제 비동기 처리 메시지 수행 ==========");

    // 컬렉션에 속한 퀴즈들 삭제
    int softDeletedQuizzes = quizRepository.softDeleteAllByCollectionId(dCollectionId);
    log.info("========== Collection ID: {} 의 퀴즈 {} 개 Soft Delete 처리 완료", dCollectionId,
        softDeletedQuizzes);

    log.info("========== 컬렉션 삭제 비동기 처리 완료 ==========");
  }

}
