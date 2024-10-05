package com.interview_master.resolver;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

import com.interview_master.domain.Access;
import com.interview_master.dto.CollectionWithAttemptsPaging;
import com.interview_master.dto.SortOrder;
import com.interview_master.service.ReadCollectionService;
import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.collection.Collection;
import com.interview_master.dto.CollectionPage;
import com.interview_master.dto.DataPage;
import com.interview_master.dto.PageInfo;
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
  public CollectionWithAttemptsPaging myCollections(@Argument DataPage paging, @Argument SortOrder sort,
      @ContextValue(required = false) Long userId,
      @ContextValue(name = "authError", required = false) String authError) {
    validateUserAuthContext(userId, authError);

    return readCollectionService.userCollections(userId, paging, sort);
  }

  /**
   * user가 시도한 컬렉션 목록(with paging)
   */
  @QueryMapping
  public CollectionPage myHistory(@Argument DataPage paging, @Argument Access filter,
      @ContextValue(required = false) Long userId,
      @ContextValue(name = "authError", required = false) String authError) {
    validateUserAuthContext(userId, authError);

    Page<Collection> collections = readCollectionService.getMyCollectionHistory(userId, paging,
        filter);

    return createCollectionPage(collections);
  }

  private static CollectionPage createCollectionPage(Page<Collection> collections) {
    return CollectionPage.builder()
        .collections(collections.getContent())
        .pageInfo(PageInfo.builder()
            .hasNextPage(collections.hasNext())
            .currentPage(collections.getNumber() + 1)
            .totalPages(collections.getTotalPages())
            .build())
        .totalCount(collections.getTotalElements())
        .build();
  }
}
