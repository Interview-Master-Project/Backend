package com.interview_master.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuizProducer {

  private final KafkaTemplate<String, Long> quizKafkaTemplate;

  public void delete(String topic, Long dQuizId) {
    log.info("========== [Producer] 퀴즈 삭제 비동기 처리 메시지 발행 ==========");
    quizKafkaTemplate.send(topic, dQuizId);
  }

}
