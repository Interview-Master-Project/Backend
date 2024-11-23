package com.interview_master.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageDeleteProducer {
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void delete(String topic, String dImageUrl) {
    log.info("========== [Producer] 이미지 삭제 비동기 처리 메시지 발행 ==========");
    kafkaTemplate.send(topic, dImageUrl);
  }

}
