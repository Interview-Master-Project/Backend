package com.interview_master.ui;

import com.interview_master.application.UpsertQuizService;
import com.interview_master.ui.request.CreateQuizInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UpsertQuizController {

    private final UpsertQuizService upsertQuizService;

    @MutationMapping
    public String createQuiz(@Argument @Valid CreateQuizInput createQuizInput) {
        upsertQuizService.saveQuiz(createQuizInput);

        return "success";
    }

    @QueryMapping
    public String deleteQuiz(@Argument Long quizId) {
        upsertQuizService.deleteQuiz(quizId);

        return "success";
    }
}
