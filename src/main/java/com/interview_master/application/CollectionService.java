package com.interview_master.application;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.collection.Collection;
import com.interview_master.infrastructure.CollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;

    public Collection findById(Long collectionId) {
        return collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));
    }

}
