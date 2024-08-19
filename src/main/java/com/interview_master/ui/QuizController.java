package com.interview_master.ui;

import com.interview_master.application.QuizService;
import com.interview_master.ui.request.CreateQuizInput;
import com.interview_master.ui.request.EditQuizInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @MutationMapping
    public String createQuiz(@Argument CreateQuizInput createQuizInput) {
        quizService.createQuiz(createQuizInput);

        return "질문 생성에 성공했습니다!";
    }

    @MutationMapping
    public String editQuiz(@Argument Long id, @Argument EditQuizInput editQuizInput) {
        log.info("quiz Id : {}\tedit quiz input : {}", id, editQuizInput);
        quizService.editQuiz(id, editQuizInput);

        return "수정에 성공했습니다!";
    }

    @MutationMapping
    public String deleteQuiz(@Argument Long id) {
        quizService.delete(id);

        return "삭제에 성공했습니다!";
    }
}
