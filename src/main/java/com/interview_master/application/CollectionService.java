package com.interview_master.application;

import com.interview_master.domain.collection.Collection;
import com.interview_master.dto.CollectionPage;
import com.interview_master.infrastructure.CollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.interview_master.common.constant.Constant.DEFAULT_PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;

    /**
     * user의 컬렉션을 offset 기반으로 페이징 + 최신 수정 순으로
     */
    @Transactional(readOnly = true)
    public CollectionPage userCollections(Long userId, Integer offset, Integer limit) {
        PaginationParams pageParams = calculatePaginationParams(offset, limit);

        Pageable pageable = PageRequest.of(pageParams.pageNumber(), pageParams.pageSize(), Sort.by("updatedAt").descending());
        Page<Collection> collectionPage = collectionRepository.findByCreatorIdAndIsDeletedFalse(userId, pageable);
        
        return CollectionPage.builder()
                .collections(collectionPage.getContent())
                .totalCount(collectionPage.getTotalElements())
                .hasNext(collectionPage.hasNext())
                .build();
    }


    /**
     * user의 히스토리 반환
     */
    @Transactional(readOnly = true)
    public CollectionPage userAttemptedCollections(Long userId, Integer offset, Integer limit) {
        PaginationParams pageParams = calculatePaginationParams(offset, limit);

        Pageable pageable = PageRequest.of(pageParams.pageNumber(), pageParams.pageSize(), Sort.by("uca.startedAt").descending());
        Page<Collection> collectionPage = collectionRepository.findAttemptedCollectionsByUserOrderByLatestAttempt(userId, pageable);

        return CollectionPage.builder()
                .collections(collectionPage.getContent())
                .totalCount(collectionPage.getTotalElements())
                .hasNext(collectionPage.hasNext())
                .build();

    }

    /**
     * 페이징 매개변수 검증하는 로직
     */
    private static PaginationParams calculatePaginationParams(Integer offset, Integer limit) {
        int pageSize = (limit != null) ? limit : DEFAULT_PAGE_SIZE;
        int pageNumber = (offset != null) ? offset / pageSize : 0;
        return new PaginationParams(pageSize, pageNumber);
    }

    private record PaginationParams(int pageSize, int pageNumber) {
    }
}
