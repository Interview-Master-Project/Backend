package com.interview_master.infrastructure;

import com.interview_master.domain.Access;
import com.interview_master.domain.collection.Collection;
import com.interview_master.dto.CollectionWithAttempts;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface CollectionRepository extends Repository<Collection, Long>, CollectionRepositoryCustom {

  Collection save(Collection collection);

  @Query("select c from Collection c "
      + "left join fetch c.quizzes q "
      + "where c.id = :id "
      + "and c.isDeleted = false "
      + "and q.isDeleted = false "
      + "order by q.updatedAt desc ")
  Optional<Collection> findByIdWithQuizzes(@Param("id") Long id);

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
      "and (:access is null or c.access = :access) " +
      "and c.isDeleted = false " +
      "group by c.id " +
      "order by max(uca.startedAt) desc")
  Page<Collection> findUserCollectionHistory(@Param("userId") Long userId,
      @Param("access") Access access, Pageable pageable);

  @Override
  Page<CollectionWithAttempts> searchCollections(List<Long> categoryIds, List<String> keywords,
      Integer maxCorrectRate, Pageable pageable, Long userId);
}
