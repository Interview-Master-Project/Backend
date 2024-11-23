package com.interview_master.kafka.consumer;

import static com.interview_master.common.constant.Constant.IMAGE_DELETE_TOPIC;
import static com.interview_master.common.constant.Constant.IMAGE_GROUP_ID;

import com.interview_master.service.NcpImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageDeleteConsumer {

  private final NcpImageService imageService;

  @KafkaListener(topics = IMAGE_DELETE_TOPIC, groupId = IMAGE_GROUP_ID, containerFactory = "imageDeleteKafkaListenerContainerFactory")
  @Transactional
  public void imageDelete(@Payload String dImageUrl) {
    log.info("========== [Consumer] 이미지 삭제 비동기 처리 메시지 수행 ==========");

    imageService.deleteImageFromBucket(dImageUrl);

    log.info("========== 이미지 삭제 비동기 처리 완료 ==========");
  }

}
