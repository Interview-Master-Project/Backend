package com.interview_master.application;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.QuizRepository;
import com.interview_master.ui.response.QuizWithCollectionAndResults;
import com.interview_master.util.ExtractUserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.interview_master.common.exception.ErrorCode.COLLECTION_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadQuizService {

    private final QuizRepository quizRepository;
    private final CollectionRepository collectionRepository;


    public Quiz findById(Long quizId) {
        Long currentUserId = ExtractUserId.extractUserIdFromContextHolder();

        return quizRepository.findByIdAndUserId(quizId, currentUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.QUIZ_NOT_FOUND));
    }

    public List<QuizWithCollectionAndResults> findMyQuizWithCollectionNameAndResults() {
        Long currentUserId = ExtractUserId.extractUserIdFromContextHolder();
        log.info("=============== current userId = {}", currentUserId);
        return quizRepository.findQuizzesWithCollectionNameAndResults(currentUserId);
    }

    public List<QuizWithCollectionAndResults> findByCollectionId(Long collectionId) {
        Long currentUserId = ExtractUserId.extractUserIdFromContextHolder();

        Collection findCollection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ApiException(COLLECTION_NOT_FOUND));

        findCollection.canAccess(currentUserId);

        return quizRepository.findByCollectionId(collectionId);
    }
}

