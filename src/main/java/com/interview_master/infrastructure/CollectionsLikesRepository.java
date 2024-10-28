package com.interview_master.infrastructure;

import com.interview_master.domain.collectionlike.CollectionsLikes;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionsLikesRepository extends JpaRepository<CollectionsLikes, Long> {

  boolean existsByCollectionIdAndUserId(Long collectionId, Long userId);

  Optional<CollectionsLikes> findByCollectionIdAndUserId(Long collectionId, Long userId);

}
