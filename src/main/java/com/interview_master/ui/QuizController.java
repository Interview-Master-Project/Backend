package com.interview_master.ui;

import com.interview_master.application.QuizService;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.ui.request.QuizInput;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
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

    @MutationMapping
    public String createQuiz(@Argument QuizInput quizInput) { // @Login User user
//        createQuizInput.setCreatorId(user.getId);
        quizService.createQuiz(quizInput);

        return "질문 생성에 성공했습니다!";
    }
}
