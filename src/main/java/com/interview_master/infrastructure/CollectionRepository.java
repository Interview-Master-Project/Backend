package com.interview_master.infrastructure;

import com.interview_master.domain.Access;
import com.interview_master.domain.collection.Collection;
import com.interview_master.dto.CollectionWithAttempt;
import com.interview_master.dto.CollectionWithLike;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface CollectionRepository extends Repository<Collection, Long>, CollectionRepositoryCustom {

  Collection save(Collection collection);

  Optional<Collection> findByIdAndIsDeletedFalse(Long id);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Collection> findWithLockByIdAndIsDeletedFalse(Long id);

  @Query("select c from Collection c "
      + "left join fetch c.quizzes q "
      + "where c.id = :id "
      + "and c.isDeleted = false "
      + "and (q is null or q.isDeleted = false) "
      + "order by case when q is not null then q.updatedAt else c.updatedAt end desc")
  Optional<Collection> findByIdWithQuizzes(@Param("id") Long id);

  /**
   * user의 컬렉션 리스트
   */
  Page<Collection> findByCreatorIdAndIsDeletedFalse(Long creatorId, Pageable pageable);

  /**
   * user가 최근에 시도한 컬렉션 리스트
   */
  @Query("select new com.interview_master.dto.CollectionWithLike(" +
      "c," +
      "case when cl.id is not null then true else false end) " +
      "from Collection c " +
      "join UserCollectionAttempt uca on uca.collection.id = c.id " +
      "left join CollectionsLikes cl on cl.collection.id = c.id and cl.user.id = :userId " +
      "where uca.user.id = :userId " +
      "and (:access is null or c.access = :access) " +
      "and c.isDeleted = false " +
      "group by c.id " +
      "order by max(uca.startedAt) desc")
  Page<CollectionWithLike> findUserCollectionHistory(@Param("userId") Long userId,
      @Param("access") Access access, Pageable pageable);

  @Override
  Page<CollectionWithAttempt> searchCollectionsForAuthUser(List<Long> categoryIds, List<String> keywords,
      Integer maxCorrectRate, Pageable pageable, Long userId);

  /**
   * user들이 생성한 컬렉션들 모두 삭제
   */
  int deleteAllByCreatorIdIn(List<Long> userIds);

  /**
   * 컬렉션 삭제
   */
  int deleteAllByIdIn(List<Long> collectionIds);

  // deleteUserScheduler 테스트 용 쿼리
  List<Collection> findByCreatorId(Long userId);

  @Query("select c.id from Collection c where c.isDeleted = true")
  List<Long> findIdsByIsDeletedTrue();

  @Modifying
  @Query("update Collection c set c.isDeleted = true where c.creator.id = :userId")
  int softDeleteAllByUserId(@Param("userId") Long userId);
}
