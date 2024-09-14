package com.interview_master.application;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.collection.Collection;
import com.interview_master.dto.CollectionWithQuizzes;
import com.interview_master.dto.QuizWithAttempts;
import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.QuizRepository;
import com.interview_master.util.ExtractUserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReadCollectionService {

    private final CollectionRepository collectionRepository;
    private final QuizRepository quizRepository;

    @Transactional(readOnly = true)
    public CollectionWithQuizzes getCollectionWithQuizzes(Long collectionId) {
        Long userId = ExtractUserId.extractUserIdFromContextHolder();

        Collection collection = collectionRepository.findByIdAndIsDeletedFalse(collectionId)
                    .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));

        // public 이거나 주인이면 접근 가능
        collection.canAccess(2L);

        List<QuizWithAttempts> quizzesWithAttempts = quizRepository.getQuizzesByCollectionIdWithAttempts(collectionId, userId);
        log.info("quizzesWithAttempts: {}", quizzesWithAttempts);
        return CollectionWithQuizzes.builder()
                .collection(collection)
                .quizzesWithAttempts(quizzesWithAttempts)
                .build();
    }
}
