package com.interview_master.application;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.infrastructure.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    public Quiz findById(Long quizId) {
        return quizRepository.findById(quizId).orElseThrow(() -> new ApiException(ErrorCode.QUIZ_NOT_FOUND));
    }
}
