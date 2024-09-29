package com.interview_master.infrastructure;

import com.interview_master.domain.collection.Collection;
import com.interview_master.dto.CollectionWithAttempts;
import com.interview_master.dto.DataPage;
import com.interview_master.dto.SortOrder;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CollectionRepository extends Repository<Collection, Long>, CollectionRepositoryCustom {

  Collection save(Collection collection);

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
      "and c.isDeleted = false " +
      "group by c.id " +
      "order by max(uca.startedAt) desc")
  Page<Collection> findAttemptedCollectionsByUserOrderByLatestAttempt(@Param("userId") Long userId,
      Pageable pageable);

  @Override
  Page<CollectionWithAttempts> searchCollections(List<Long> categoryIds, List<String> keywords,
      Integer maxCorrectRate, Pageable pageable, Long userId);
}
