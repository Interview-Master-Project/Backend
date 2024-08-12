package com.interview_master.application;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.infrastructure.QuizRepository;
import com.interview_master.ui.request.QuizInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    public Quiz findById(Long quizId) {
        return quizRepository.findById(quizId)
            .orElseThrow(() -> new ApiException(ErrorCode.QUIZ_NOT_FOUND));
    }

    @Transactional
    public void createQuiz(QuizInput quizInput) {
        Quiz newQuiz = new Quiz(quizInput.getCollectionId(), quizInput.getQuestion(),
            quizInput.getAnswer(), quizInput.getCreatorId(),
            quizInput.getAccess());

        quizRepository.save(newQuiz);
    }
}
