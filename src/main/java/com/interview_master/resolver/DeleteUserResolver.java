package com.interview_master.resolver;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

import com.interview_master.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DeleteUserResolver {

  private final UserProfileService userProfileService;

  @MutationMapping
  public String deleteUser(@ContextValue(required = false) Long userId,
      @ContextValue(required = false) String authError) {

    validateUserAuthContext(userId, authError);
    userProfileService.deleteUser(userId);
    return "success";
  }

}
