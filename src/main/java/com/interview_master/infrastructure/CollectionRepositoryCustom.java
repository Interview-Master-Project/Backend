package com.interview_master.infrastructure;

import com.interview_master.dto.CollectionWithAttempts;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CollectionRepositoryCustom {

  Page<CollectionWithAttempts> searchCollections(List<Long> categoryIds,
      List<String> keywords,
      Integer maxCorrectRate, Pageable pageable, Long userId);

}
