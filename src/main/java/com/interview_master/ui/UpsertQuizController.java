package com.interview_master.ui;

import com.interview_master.application.UpsertQuizService;
import com.interview_master.ui.request.CreateQuizInput;
import com.interview_master.ui.request.EditQuizInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UpsertQuizController {

    private final UpsertQuizService upsertQuizService;

    @MutationMapping
    public String createQuiz(@Argument @Valid CreateQuizInput createQuizInput,
                             @ContextValue(required = false) Long userId,
                             @ContextValue(name = "authError", required = false) String authError) {

        validateUserAuthContext(userId, authError);
        upsertQuizService.saveQuiz(createQuizInput, userId);

        return "success";
    }

    @MutationMapping
    public String editQuiz(@Argument Long quizId, @Argument EditQuizInput editQuizInput,
                           @ContextValue(required = false) Long userId,
                           @ContextValue(name = "authError", required = false) String authError) {

        validateUserAuthContext(userId, authError);
        upsertQuizService.editQuiz(quizId, editQuizInput, userId);

        return "success";
    }

    @QueryMapping
    public String deleteQuiz(@Argument Long quizId,
                             @ContextValue(required = false) Long userId,
                             @ContextValue(name = "authError", required = false) String authError) {

        validateUserAuthContext(userId, authError);
        upsertQuizService.deleteQuiz(quizId, userId);

        return "success";
    }
}
