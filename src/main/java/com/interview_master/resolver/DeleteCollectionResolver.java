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
public class DeleteCollectionResolver {

  private final UpsertCollectionService upsertCollectionService;

  @MutationMapping
  public String deleteCollection(@Argument Long collectionId,
      @ContextValue(required = false) Long userId,
      @ContextValue(required = false) String authError) {

    validateUserAuthContext(userId, authError);

    upsertCollectionService.deleteCollection(collectionId, userId);

    return "success";
  }

}
