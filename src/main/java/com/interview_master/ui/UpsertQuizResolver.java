package com.interview_master.ui;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

import com.interview_master.service.UpsertQuizService;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.ui.request.CreateQuizInput;
import com.interview_master.ui.request.EditQuizInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UpsertQuizResolver {

  private final UpsertQuizService upsertQuizService;

  @MutationMapping
  public Quiz createQuiz(@Argument @Valid CreateQuizInput createQuizInput,
      @ContextValue(required = false) Long userId,
      @ContextValue(name = "authError", required = false) String authError) {

    validateUserAuthContext(userId, authError);

    return upsertQuizService.saveQuiz(createQuizInput, userId);
  }

  @MutationMapping
  public Quiz editQuiz(@Argument Long quizId, @Argument EditQuizInput editQuizInput,
      @ContextValue(required = false) Long userId,
      @ContextValue(name = "authError", required = false) String authError) {

    validateUserAuthContext(userId, authError);
    return upsertQuizService.editQuiz(quizId, editQuizInput, userId);
  }

  @MutationMapping
  public String deleteQuiz(@Argument Long quizId,
      @ContextValue(required = false) Long userId,
      @ContextValue(name = "authError", required = false) String authError) {

    validateUserAuthContext(userId, authError);
    upsertQuizService.deleteQuiz(quizId, userId);

    return "success";
  }
}
