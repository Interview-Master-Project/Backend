package com.interview_master.resolver;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

import com.interview_master.service.UserCollectionAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DeleteAttemptResolver {

  private final UserCollectionAttemptService userCollectionAttemptService;

  @MutationMapping
  public String deleteRecentAttempt(@Argument Long userCollectionAttemptId,
      @ContextValue(required = false) Long userId,
      @ContextValue(required = false) String authError) {

    validateUserAuthContext(userId, authError);

    userCollectionAttemptService.deleteAttempt(userCollectionAttemptId, userId);

    return "success";
  }

}
