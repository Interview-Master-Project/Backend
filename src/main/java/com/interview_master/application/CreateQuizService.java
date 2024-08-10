package com.interview_master.application;

import com.interview_master.domain.quiz.Quiz;
import com.interview_master.domain.user.User;
import com.interview_master.infrastructure.QuizRepository;
import com.interview_master.ui.QuizRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateQuizService {

    private final QuizRepository quizRepository;

    @Transactional
    public void createQuiz(QuizRequest quizRequest, User user) {
        Quiz newQuiz = new Quiz(quizRequest.getCollectionId(), quizRequest.getQuestion(),
                quizRequest.getAnswer(), user.getId(), quizRequest.getAccess());

        quizRepository.save(newQuiz);
    }

}
