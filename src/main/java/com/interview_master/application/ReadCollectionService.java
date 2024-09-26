package com.interview_master.application;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.collection.Collection;
import com.interview_master.dto.CollectionPage;
import com.interview_master.dto.CollectionWithQuizzes;
import com.interview_master.dto.QuizWithAttempts;
import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public CollectionWithQuizzes getCollectionWithQuizzes(Long collectionId, Long userId) {
        Collection collection = collectionRepository.findByIdAndIsDeletedFalse(collectionId)
                    .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));

        // public 이거나 주인이면 접근 가능
        collection.canAccess(userId);

        List<QuizWithAttempts> quizzesWithAttempts = quizRepository.getQuizzesByCollectionIdWithAttempts(collectionId, userId);
        return CollectionWithQuizzes.builder()
                .collection(collection)
                .quizzesWithAttempts(quizzesWithAttempts)
                .build();
    }

    /**
     * user의 컬렉션을 offset 기반으로 페이징 + 최신 수정 순으로
     */
    @Transactional(readOnly = true)
    public Page<Collection> userCollections(Long userId, Integer start, Integer first) {
        Pageable pageable = PageRequest.of(start / first, first, Sort.by("updatedAt").descending());
        return collectionRepository.findByCreatorIdAndIsDeletedFalse(userId,
            pageable);
    }

    /**
     * user의 히스토리 반환
     */
    @Transactional(readOnly = true)
    public Page<Collection> userAttemptedCollections(Long userId, Integer start, Integer first) {
        Pageable pageable = PageRequest.of(start / first, first);
        return collectionRepository.findAttemptedCollectionsByUserOrderByLatestAttempt(
            userId, pageable);
    }
}
