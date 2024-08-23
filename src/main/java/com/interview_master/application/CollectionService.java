package com.interview_master.application;

import com.interview_master.domain.Access;
import com.interview_master.domain.collection.Collection;
import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.ui.request.CreateCollectionInput;
import com.interview_master.util.ExtractUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.interview_master.domain.Access.PUBLIC;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;

    @Transactional
    public void createCollection(CreateCollectionInput input) {
        Long currentUserId = ExtractUserId.extractUserIdFromContextHolder();

        // 기본값을 설정
        Long categoryId = input.getCategoryId() != null ? input.getCategoryId() : 999L;
        Access access = input.getAccess() != null ? input.getAccess() : PUBLIC;

        Collection newCollection = Collection.builder()
                .name(input.getName())
                .categoryId(categoryId)
                .access(access)
                .creatorId(currentUserId)
                .build();

        collectionRepository.save(newCollection);
    }
}
