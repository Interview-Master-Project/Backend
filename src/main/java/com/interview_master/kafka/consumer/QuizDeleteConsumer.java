package com.interview_master.kafka.consumer;

import static com.interview_master.common.constant.Constant.QUIZ_DELETE_TOPIC;
import static com.interview_master.common.constant.Constant.QUIZ_GROUP_ID;

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


  @KafkaListener(topics = QUIZ_DELETE_TOPIC, groupId = QUIZ_GROUP_ID, containerFactory = "defaultKafkaListenerContainerFactory")
  @Transactional
  public void quizDelete(@Payload Long dQuizId) {
    log.info("========== [Consumer] 퀴즈 삭제 비동기 처리 메시지 수행 ==========");


    log.info("========== 컬렉션 삭제 비동기 처리 완료 ==========");
  }

}
