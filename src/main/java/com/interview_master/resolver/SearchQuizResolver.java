package com.interview_master.resolver;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

import com.interview_master.dto.DataPage;
import com.interview_master.dto.PageInfo;
import com.interview_master.dto.QuizWithAttempt;
import com.interview_master.dto.QuizzesWithAttemptPaging;
import com.interview_master.dto.SortOrder;
import com.interview_master.service.SearchQuizService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SearchQuizResolver {

  private final SearchQuizService searchQuizService;

  @QueryMapping
  public QuizzesWithAttemptPaging searchQuizzes(@Argument List<Long> categoryIds,
      @Argument List<String> keywords,
      @Argument Integer maxCorrectRate, @Argument DataPage paging,
      @Argument SortOrder sort, @ContextValue(required = false) Long userId,
      @ContextValue(name = "authError", required = false) String authError) {

    validateUserAuthContext(userId, authError);

    Page<QuizWithAttempt> result = searchQuizService.searchQuizzes(categoryIds, keywords,
        maxCorrectRate, paging, sort, userId);
    return QuizzesWithAttemptPaging.builder()
        .quizzesWithAttempt(result.getContent())
        .pageInfo(PageInfo.builder()
            .currentPage(result.getNumber() + 1)
            .hasNextPage(result.hasNext())
            .totalPages(result.getTotalPages())
            .build()
        )
        .build();
  }
}
