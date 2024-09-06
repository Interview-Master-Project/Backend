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

import java.util.List;

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
        int pageSize = (limit != null) ? limit : DEFAULT_PAGE_SIZE;
        int pageNumber = (offset != null) ? offset / pageSize : 0;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("updatedAt").descending());
        Page<Collection> collectionPage = collectionRepository.findByCreatorIdAndIsDeletedFalse(userId, pageable);

        List<Collection> collections = collectionPage.getContent();
        long totalCount = collectionPage.getTotalElements();    // 전체 데이터 수
        boolean hasNext = collectionPage.hasNext(); // 다음 페이지 존재 여부

        return CollectionPage.builder()
                .collections(collections)
                .totalCount(totalCount)
                .hasNext(hasNext)
                .build();
    }
}
