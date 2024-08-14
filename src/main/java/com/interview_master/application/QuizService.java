package com.interview_master.application;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.infrastructure.QuizRepository;
import com.interview_master.ui.request.QuizInput;
import com.interview_master.ui.response.QuizWithCollectionNameAndResults;
import com.interview_master.util.ExtractUserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    public Quiz findById(Long quizId) {
        Long currentUserId = ExtractUserId.extractUserIdFromContextHolder();

        return quizRepository.findByIdAndUserId(quizId, currentUserId)
            .orElseThrow(() -> new ApiException(ErrorCode.QUIZ_NOT_FOUND));
    }

    @Transactional
    public void createQuiz(QuizInput quizInput) {
        Long currentUserId = ExtractUserId.extractUserIdFromContextHolder();

        Quiz newQuiz = new Quiz(quizInput.getCollectionId(), quizInput.getQuestion(),
            quizInput.getAnswer(), currentUserId, quizInput.getAccess());

        quizRepository.save(newQuiz);
    }

    public List<QuizWithCollectionNameAndResults> findMyQuizWithCollectionNameAndResults() {
        Long currentUserId = ExtractUserId.extractUserIdFromContextHolder();
        log.info("=============== current userId = {}", currentUserId);
        return quizRepository.findQuizzesWithCollectionNameAndResults(currentUserId);
    }
}
