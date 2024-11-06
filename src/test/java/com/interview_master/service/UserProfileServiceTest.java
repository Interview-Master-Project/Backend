package com.interview_master.service;

import com.interview_master.domain.user.User;
import com.interview_master.infrastructure.UserRepository;
import com.interview_master.login.OAuthProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.yml")
public class UserProfileServiceTest {

  @Autowired
  private UserProfileService userProfileService;

  @Autowired
  private UserRepository userRepository;

  @Test
  void deleteUser() {
    Long userId = 1L;
    User user = User.builder()
        .id(userId)
        .email("test@test.com")
        .nickname("nick")
        .oAuthProvider(OAuthProvider.KAKAO)
        .build();
    userRepository.save(user);

    userProfileService.deleteUser(userId);

    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    System.out.println(findUser.getEmail() + "\t" + findUser.getIsDeleted());
    Assertions.assertThat(findUser.getIsDeleted()).isTrue();
  }
}