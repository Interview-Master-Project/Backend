package com.interview_master.ui;

import com.interview_master.application.QuizService;
import com.interview_master.application.ReadQuizService;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.ui.request.CreateQuizInput;
import java.util.List;

import com.interview_master.ui.request.EditQuizInput;
import com.interview_master.ui.response.QuizWithCollectionAndResults;
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
    private final ReadQuizService readQuizService;

    @QueryMapping
    public Quiz quizById(@Argument Long id) {
        return readQuizService.findById(id);
    }

    @MutationMapping
    public String createQuiz(@Argument CreateQuizInput createQuizInput) {
        quizService.createQuiz(createQuizInput);

        return "질문 생성에 성공했습니다!";
    }

    @QueryMapping
    public List<QuizWithCollectionAndResults> getMyQuiz() {
        return readQuizService.findMyQuizWithCollectionNameAndResults();
    }

    @QueryMapping
    public List<QuizWithCollectionAndResults> quizByCollectionId(@Argument Long id) {
        return readQuizService.findByCollectionId(id);
    }

    @MutationMapping
    public String editQuiz(@Argument Long id, @Argument EditQuizInput editQuizInput) {
        log.info("quiz Id : {}\tedit quiz input : {}", id, editQuizInput);
        quizService.editQuiz(id, editQuizInput);

        return "수정에 성공했습니다!";
    }
}
