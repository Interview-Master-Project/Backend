package com.interview_master.application;

import com.interview_master.domain.quiz.Quiz;
import com.interview_master.domain.user.User;
import com.interview_master.infrastructure.QuizRepository;
import com.interview_master.ui.request.QuizInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateQuizService {

    private final QuizRepository quizRepository;

    @Transactional
    public void createQuiz(QuizInput quizInput, User user) {
        Quiz newQuiz = new Quiz(quizInput.getCollectionId(), quizInput.getQuestion(),
                quizInput.getAnswer(), user.getId(), quizInput.getAccess());

        quizRepository.save(newQuiz);
    }

}
