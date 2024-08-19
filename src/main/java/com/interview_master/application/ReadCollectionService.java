package com.interview_master.application;

import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.ui.response.CollectionWithCategoryAndQuizCount;
import com.interview_master.util.ExtractUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadCollectionService {

    private final CollectionRepository collectionRepository;

    public List<CollectionWithCategoryAndQuizCount> getMyCollections() {
        Long currentUserId = ExtractUserId.extractUserIdFromContextHolder();

        return collectionRepository.findCollectionsByUserId(currentUserId);

    }

}