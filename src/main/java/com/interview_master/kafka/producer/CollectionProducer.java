package com.interview_master.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CollectionProducer {

  private final KafkaTemplate<String, Long> collectionKafkaTemplate;

  public void delete(String topic, Long dCollectionId) {
    log.info("========== [Producer] 컬렉션 삭제 비동기 처리 메시지 발행 ==========");
    collectionKafkaTemplate.send(topic, dCollectionId);
  }

}
