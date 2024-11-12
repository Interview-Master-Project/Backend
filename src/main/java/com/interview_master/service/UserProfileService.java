package com.interview_master.service;

import static com.interview_master.common.constant.Constant.USER_DELETE_TOPIC;

import com.interview_master.common.constant.Constant;
import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.user.User;
import com.interview_master.infrastructure.UserRepository;
import com.interview_master.kafka.producer.UserProducer;
import com.interview_master.resolver.request.EditUserReq;
import com.interview_master.util.ExtractUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

  private final UserRepository userRepository;
  private final NcpImageService imageService;

  private final UserProducer userProducer;

  public User getProfile(Long userId) {
    return userRepository.findByIdAndIsDeletedFalse(userId)
        .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
  }

  @Transactional
  public User editProfile(EditUserReq editUserReq) {
    Long userId = ExtractUserId.extractUserIdFromContextHolder();

    User user = userRepository.findByIdAndIsDeletedFalse(userId)
        .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

    String newImgUrl = user.getImgUrl();

    // 이미지만 삭제하는 경우
    if (Boolean.TRUE.equals(editUserReq.getDeleteImageOnly())) {
      if (user.getImgUrl() != null) {
        imageService.deleteImageFromBucket(user.getImgUrl());
      }
      newImgUrl = null;
    }
    else {
      // 새 이미지 업로드하는 경우
      if (editUserReq.getImage() != null) {
        // 기존 이미지가 있으면 삭제
        if (user.getImgUrl() != null) {
          imageService.deleteImageFromBucket(user.getImgUrl());
        }
        newImgUrl = imageService.uploadImage(editUserReq.getImage());
      }
    }

    user.edit(editUserReq.getName(), newImgUrl);

    return user;
  }

  @Transactional
  public void deleteUser(Long userId) {
    User user = userRepository.findByIdAndIsDeletedFalse(userId)
        .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

    user.markDeleted();

    // user 탈퇴 비동기 처리 메시지 발행
    userProducer.delete(USER_DELETE_TOPIC, user);
  }
}
