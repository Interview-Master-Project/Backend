package com.interview_master.application;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.domain.user.User;
import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.QuizRepository;
import com.interview_master.infrastructure.UserRepository;
import com.interview_master.ui.request.CreateQuizInput;
import com.interview_master.util.ExtractUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpsertQuizService {

    private final QuizRepository quizRepository;
    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;

    public void saveQuiz(CreateQuizInput createQuizInput) {
        Long userId = ExtractUserId.extractUserIdFromContextHolder();

        // collection 검증
        Collection collection = collectionRepository.findByIdAndIsDeletedFalse(createQuizInput.getCollectionId())
                .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));
        collection.isOwner(userId);

        // 유저 존재 여부 검증
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        // 새 퀴즈 생성
        Quiz newQuiz = new Quiz(
                createQuizInput.getQuestion(),
                createQuizInput.getAnswer(),
                collection,
                user,
                collection.getAccess()
        );

        // 저장
        quizRepository.save(newQuiz);
    }
}