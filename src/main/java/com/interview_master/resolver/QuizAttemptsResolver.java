package com.interview_master.resolver;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

import com.interview_master.domain.userquizattempt.UserQuizAttempt;
import com.interview_master.service.UserQuizAttemptService;
import com.interview_master.dto.QuizGarden;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class QuizAttemptsResolver {

  private final UserQuizAttemptService userQuizAttemptService;

  @QueryMapping
  public List<QuizGarden> getQuizGarden(@Argument LocalDate startDate, @Argument LocalDate endDate,
      @ContextValue(required = false) Long userId,
      @ContextValue(name = "authError", required = false) String authError) {

    validateUserAuthContext(userId, authError);

    return userQuizAttemptService.getQuizGardens(startDate, endDate, userId);
  }

  @QueryMapping
  public List<UserQuizAttempt> getUserQuizAttempts(@Argument Long quizId,
      @ContextValue(required = false) Long userId,
      @ContextValue(name = "authError", required = false) String authError) {
    validateUserAuthContext(userId, authError);

    return userQuizAttemptService.getUserQuizAttempts(quizId, userId);
  }
}
