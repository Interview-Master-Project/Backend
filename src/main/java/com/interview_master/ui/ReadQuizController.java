package com.interview_master.ui;

import com.interview_master.application.ReadQuizService;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.ui.response.QuizWithCollectionAndResults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReadQuizController {

    private final ReadQuizService readQuizService;

    @QueryMapping
    public Quiz quizById(@Argument Long quizId) {
        return readQuizService.findById(quizId);
    }

    @QueryMapping
    public List<QuizWithCollectionAndResults> getMyQuiz() {
        return readQuizService.findMyQuizWithCollectionNameAndResults();
    }

    @QueryMapping
    public List<QuizWithCollectionAndResults> getQuizzesByCollectionId(@Argument Long collectionId) {
        return readQuizService.findByCollectionId(collectionId);
    }
}