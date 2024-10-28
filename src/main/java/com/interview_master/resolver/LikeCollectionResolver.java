package com.interview_master.resolver;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

import com.interview_master.service.UpsertCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LikeCollectionResolver {

  private final UpsertCollectionService upsertCollectionService;


  @MutationMapping
  public String like(@Argument Long collectionId, @ContextValue(required = false) Long userId,
      @ContextValue(name = "authError", required = false) String authError) {
    validateUserAuthContext(userId, authError);

    upsertCollectionService.likeCollection(collectionId, userId);

    return "success";
  }
}
