package com.interview_master.ui;

import com.interview_master.application.QuizService;
import com.interview_master.domain.quiz.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @QueryMapping
    public Quiz quizById(@Argument Long id) {
        return quizService.findById(id);
    }
}
