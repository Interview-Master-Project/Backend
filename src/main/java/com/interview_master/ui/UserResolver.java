package com.interview_master.ui;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

import com.interview_master.application.UserProfileService;
import com.interview_master.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserResolver {

  private final UserProfileService userProfileService;

  @QueryMapping
  public User me(@ContextValue(required = false) Long userId,
      @ContextValue(name = "authError", required = false) String authError) {

    validateUserAuthContext(userId, authError);

    log.info("========== me\t userId: {}", userId);

    return userProfileService.getProfile(userId);
  }
}