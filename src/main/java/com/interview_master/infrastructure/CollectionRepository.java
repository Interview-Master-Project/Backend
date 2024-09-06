package com.interview_master.infrastructure;

import com.interview_master.domain.collection.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface CollectionRepository extends Repository<Collection, Long> {
    Page<Collection> findByCreatorIdAndIsDeletedFalse(Long creatorId, Pageable pageable);
}
