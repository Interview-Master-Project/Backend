package com.interview_master.resolver;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

import com.interview_master.domain.usercollectionattempt.UserCollectionAttempt;
import com.interview_master.service.UserCollectionAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SolveCollectionResolver {

  private final UserCollectionAttemptService userCollectionAttemptService;

  @MutationMapping
  public UserCollectionAttempt startSolveCollection(@Argument Long collectionId,
      @ContextValue(required = false) Long userId,
      @ContextValue(name = "authError", required = false) String authError) {

    validateUserAuthContext(userId, authError);

    return userCollectionAttemptService.startSolveCollection(collectionId, userId);
  }

}
