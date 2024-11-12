package com.interview_master.kafka.consumer;

import static com.interview_master.common.constant.Constant.USER_DELETE_TOPIC;
import static com.interview_master.common.constant.Constant.USER_GROUP_ID;

import com.interview_master.domain.user.User;
import com.interview_master.infrastructure.CollectionRepository;
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
public class UserDeleteConsumer {

  private final CollectionRepository collectionRepository;
  private final QuizRepository quizRepository;

  @KafkaListener(topics = USER_DELETE_TOPIC, groupId = USER_GROUP_ID, containerFactory = "userDeleteKafkaListenerContainerFactory")
  @Transactional
  public void userDelete(@Payload User dUser) {
    log.info("========== [Consumer] 유저 탈퇴 비동기 처리 메시지 수행 ==========");
    // 유저가 만든 collection들 soft delete
    int softDeletedCollections = collectionRepository.softDeleteAllByUserId(dUser.getId());
    log.info("========== 사용자 ID: {} 의 컬렉션 {} 개 Soft Delete 처리 완료", dUser.getId(),
        softDeletedCollections);

    // 유저가 만든 quiz들 soft delete
    int softDeletedQuizzes = quizRepository.softDeleteAllByUserId(dUser.getId());
    log.info("========== 사용자 ID: {} 의 퀴즈 {} 개 Soft Delete 처리 완료", dUser.getId(),
        softDeletedQuizzes);

    int total = softDeletedCollections + softDeletedQuizzes;
    log.info("========== 사용자 ID: {} 의 총 {} 개의 데이터 Soft Delete 처리 완료", dUser.getId(), total);

    log.info("========== 유저 탈퇴 비동기 처리 완료 ==========");
  }

}
