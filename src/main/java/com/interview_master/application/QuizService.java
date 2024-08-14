package com.interview_master.application;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.infrastructure.QuizRepository;
import com.interview_master.ui.request.QuizInput;
import com.interview_master.util.ExtractUserId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Quiz> findMyQuiz() {
        Long currentUserId = ExtractUserId.extractUserIdFromContextHolder();
        return quizRepository.findByCreatorIdOrderByIdDesc(currentUserId);
    }
}
