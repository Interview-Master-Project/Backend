package com.interview_master.infrastructure;

import com.interview_master.domain.Access;
import com.interview_master.dto.CollectionWithAttempt;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CollectionRepositoryCustom {

  Page<CollectionWithAttempt> searchCollectionsForAuthUser(List<Long> categoryIds,
      List<String> keywords,
      Integer maxCorrectRate, Pageable pageable, Long userId);

  Page<CollectionWithAttempt> searchCollectionsForGuest(List<Long> categoryIds,
      List<String> keywords, Pageable pageable);

  Page<CollectionWithAttempt> myCollections(Long userId, Pageable pageable);

  Page<CollectionWithAttempt> getHistory (Long userId, Access access, Pageable pageable);
}
