package com.interview_master.resolver;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

import com.interview_master.dto.CollectionWithAttempt;
import com.interview_master.dto.CollectionWithAttemptsPaging;
import com.interview_master.dto.DataPage;
import com.interview_master.dto.PageInfo;
import com.interview_master.dto.SortOrder;
import com.interview_master.service.SearchCollectionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SearchCollectionResolver {

  private final SearchCollectionService searchCollectionService;

  @QueryMapping
  public CollectionWithAttemptsPaging searchCollectionsForAuthUser(
      @Argument List<Long> categoryIds, @Argument List<String> keywords,
      @Argument Integer maxCorrectRate, @Argument DataPage paging,
      @Argument SortOrder sort, @ContextValue(required = false) Long userId,
      @ContextValue(name = "authError", required = false) String authError) {

    validateUserAuthContext(userId, authError);

    Page<CollectionWithAttempt> result = searchCollectionService.searchCollectionsForAuthUser(
        categoryIds, keywords, maxCorrectRate, paging, sort, userId);

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
  public CollectionWithAttemptsPaging searchCollectionsForGuest(
      @Argument List<Long> categoryIds, @Argument List<String> keywords,
      @Argument DataPage paging) {

    Page<CollectionWithAttempt> result = searchCollectionService.searchCollectionsForGuest(
        categoryIds, keywords, paging);

    return CollectionWithAttemptsPaging.builder()
        .collectionsWithAttempt(result.getContent())
        .pageInfo(PageInfo.builder()
            .currentPage(result.getNumber() + 1)
            .hasNextPage(result.hasNext())
            .totalPages(result.getTotalPages())
            .build())
        .build();
  }
}
