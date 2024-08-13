package com.interview_master.ui;

import com.interview_master.application.QuizService;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.ui.request.QuizInput;
import com.interview_master.util.ExtractUserId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @QueryMapping
    public Quiz quizById(@Argument Long id) {
        return quizService.findById(id);
    }

    @MutationMapping
    public String createQuiz(@Argument QuizInput quizInput) {
        quizService.createQuiz(quizInput);

        return "질문 생성에 성공했습니다!";
    }

    @QueryMapping
    public List<Quiz> getMyQuiz() {
        Long currentUserId = ExtractUserId.extractUserIdFromContextHolder();
        log.info("======== getMyQuiz : {}", currentUserId);
        return quizService.findByUserId(currentUserId);
    }
}
