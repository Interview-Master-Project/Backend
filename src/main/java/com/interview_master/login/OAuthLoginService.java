package com.interview_master.login;

import com.interview_master.domain.user.User;
import com.interview_master.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

  private final UserRepository userRepository;
  private final RequestOAuthInfoService requestOAuthInfoService;

  public User login(OAuthLoginParams params) {
    OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
    return findOrCreateUser(oAuthInfoResponse);
  }

  private User findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {
    return userRepository.findByEmailAndIsDeletedFalse(oAuthInfoResponse.getEmail())
        .orElseGet(() -> newUser(oAuthInfoResponse));
  }

  private User newUser(OAuthInfoResponse oAuthInfoResponse) {
    User newUser = User.builder()
        .email(oAuthInfoResponse.getEmail())
        .nickname(oAuthInfoResponse.getNickname())
        .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
        .build();
    return userRepository.save(newUser);
  }
}