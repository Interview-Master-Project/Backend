package com.interview_master.infrastructure;

import com.interview_master.domain.collection.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CollectionRepository extends Repository<Collection, Long> {

    void save(Collection collection);

    Optional<Collection> findByIdAndIsDeletedFalse(Long id);
    /**
     * user의 컬렉션 리스트
     */
    Page<Collection> findByCreatorIdAndIsDeletedFalse(Long creatorId, Pageable pageable);

    /**
     * user가 최근에 시도한 컬렉션 리스트
     */
    @Query("select c from Collection c " +
            "join UserCollectionAttempt uca on uca.collection.id = c.id " +
            "where uca.user.id = :userId " +
            "and c.isDeleted = false")
    Page<Collection> findAttemptedCollectionsByUserOrderByLatestAttempt(@Param("userId") Long userId, Pageable pageable);

}
