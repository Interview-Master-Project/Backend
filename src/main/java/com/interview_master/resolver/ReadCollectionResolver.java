package com.interview_master.resolver;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.Access;
import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.usercollectionattempt.UserCollectionAttempt;
import com.interview_master.dto.CollectionWithAttempt;
import com.interview_master.dto.CollectionWithAttemptsPaging;
import com.interview_master.dto.DataPage;
import com.interview_master.dto.PageInfo;
import com.interview_master.dto.SortOrder;
import com.interview_master.service.ReadCollectionService;
import com.interview_master.service.UserCollectionAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ReadCollectionResolver {

  private final ReadCollectionService readCollectionService;
  private final UserCollectionAttemptService userCollectionAttemptService;

  /**
   * 컬렉션 조회
   */
  @QueryMapping
  public Collection getCollection(@Argument Long collectionId,
      @ContextValue(required = false) Long userId) {
    if (userId == null) {
      throw new ApiException(ErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
    }

    return readCollectionService.getCollectionById(collectionId);
  }

  /**
   * user의 컬렉션 목록(with paging)
   */
  @QueryMapping
  public CollectionWithAttemptsPaging myCollections(@Argument DataPage paging,
      @Argument SortOrder sort,
      @ContextValue(required = false) Long userId,
      @ContextValue(name = "authError", required = false) String authError) {
    validateUserAuthContext(userId, authError);

    Page<CollectionWithAttempt> result = readCollectionService.userCollections(
        userId, paging, sort);

    return CollectionWithAttemptsPaging.builder()
        .collectionsWithAttempt(result.getContent())
        .pageInfo(PageInfo.builder()
            .currentPage(result.getNumber() + 1)
            .hasNextPage(result.hasNext())
            .totalPages(result.getTotalPages())
            .build())
        .build();
  }

  /**
   * user가 시도한 컬렉션 목록(with paging)
   */
  @QueryMapping
  public CollectionWithAttemptsPaging myHistory(@Argument DataPage paging, @Argument Access filter,
      @ContextValue(required = false) Long userId,
      @ContextValue(name = "authError", required = false) String authError) {
    validateUserAuthContext(userId, authError);

    Page<CollectionWithAttempt> result = readCollectionService.getMyCollectionHistory(
        userId, paging,
        filter);

    return CollectionWithAttemptsPaging.builder()
        .collectionsWithAttempt(result.getContent())
        .pageInfo(PageInfo.builder()
            .currentPage(result.getNumber() + 1)
            .hasNextPage(result.hasNext())
            .totalPages(result.getTotalPages())
            .build())
        .build();
  }

  @QueryMapping
  public UserCollectionAttempt getLatestCollectionAttempt(@Argument Long collectionId,
      @ContextValue(required = false) Long userId,
      @ContextValue(name = "authError", required = false) String authError) {

    validateUserAuthContext(userId, authError);

    return userCollectionAttemptService.getLatestCollectionAttempt(collectionId, userId);
  }
}
