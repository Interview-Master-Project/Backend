package com.interview_master.resolver;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

import com.interview_master.dto.QuizWithAttempt;
import com.interview_master.infrastructure.QuizRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class QuizWithAttemptsResolver {

  private final QuizRepository quizRepository;

  /**
   * collection에 속한 퀴즈 목록과 각 퀴즈에 대한 현재 유저가 시도한 정보들
   */
  @QueryMapping
  public List<QuizWithAttempt> getQuizzesWithAttemptByCollectionId(@Argument Long collectionId,
      @ContextValue(required = false) Long userId,
      @ContextValue(name = "authError", required = false) String authError) {
    validateUserAuthContext(userId, authError);

    return quizRepository.getQuizzesByCollectionIdWithAttempts(collectionId, userId);
  }

}
