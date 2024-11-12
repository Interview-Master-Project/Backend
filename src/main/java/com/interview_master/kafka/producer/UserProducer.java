package com.interview_master.kafka.producer;

import com.interview_master.common.constant.Constant;
import com.interview_master.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserProducer {

  private final KafkaTemplate<String, User> userKafkaTemplate;

  public void delete(String topic, User dUser) {
    log.info("========== [Producer] 유저 탈퇴 비동기 처리 메시지 발행 ==========");
    userKafkaTemplate.send(Constant.USER_DELETE_TOPIC, dUser);
  }

}
