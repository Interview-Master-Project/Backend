package com.interview_master.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CollectionAttemptProducer {

  private final KafkaTemplate<String, Long> caKafkaTemplate;

  public void delete(String topic, Long dCollectionAttemptId) {
    log.info("========== [Producer] 컬렉션 시도 기록 삭제 비동기 처리 메시지 발행 ==========");
    caKafkaTemplate.send(topic, dCollectionAttemptId);
  }

}
