package com.interview_master.ui;

import com.interview_master.application.UpsertQuizService;
import com.interview_master.ui.request.CreateQuizInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CreateQuizController {

    private final UpsertQuizService upsertQuizService;

    @MutationMapping
    public String createQuiz(@Argument @Valid CreateQuizInput createQuizInput) {
        upsertQuizService.saveQuiz(createQuizInput);

        return "success";
    }
}
