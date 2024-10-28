package com.interview_master.infrastructure;

import com.interview_master.domain.collectionlike.CollectionsLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionsLikesRepository extends JpaRepository<CollectionsLikes, Long> {

  boolean existsByCollectionIdAndUserIdAndLikedIsTrue(Long collectionId, Long userId);
}
