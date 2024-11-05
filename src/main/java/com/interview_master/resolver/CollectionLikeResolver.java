package com.interview_master.resolver;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

import com.interview_master.infrastructure.CollectionsLikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CollectionLikeResolver {

  private final CollectionsLikesRepository collectionsLikesRepository;

  @QueryMapping
  public Boolean isLiked(@Argument Long collectionId, @ContextValue(required = false) Long userId,
      @ContextValue(required = false) String authError) {

    validateUserAuthContext(userId, authError);

    return collectionsLikesRepository.existsByCollectionIdAndUserId(collectionId, userId);
  }
}
