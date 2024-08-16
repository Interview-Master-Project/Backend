package com.interview_master.application;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.infrastructure.QuizRepository;
import com.interview_master.ui.request.CreateQuizInput;
import com.interview_master.ui.request.EditQuizInput;
import com.interview_master.util.ExtractUserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    @Transactional
    public void createQuiz(CreateQuizInput quizInput) {
        Long currentUserId = ExtractUserId.extractUserIdFromContextHolder();

        Quiz newQuiz = new Quiz(quizInput.getCollectionId(), quizInput.getQuestion(),
            quizInput.getAnswer(), currentUserId, quizInput.getAccess());

        quizRepository.save(newQuiz);
    }

    @Transactional
    public void editQuiz(Long quizId, EditQuizInput editQuizInput) {
        Long currentUserId = ExtractUserId.extractUserIdFromContextHolder();

        Quiz findQuiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ApiException(ErrorCode.QUIZ_NOT_FOUND));

        checkEditPermission(currentUserId, findQuiz);
        findQuiz.edit(editQuizInput);
    }

    private void checkEditPermission(Long userId, Quiz quiz) {
        if (!userId.equals(quiz.getCreatorId())) {
            throw new ApiException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }
}
