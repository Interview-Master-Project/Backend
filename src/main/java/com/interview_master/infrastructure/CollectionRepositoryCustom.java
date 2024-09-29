package com.interview_master.infrastructure;

import com.interview_master.dto.CollectionWithAttempts;
import com.interview_master.dto.DataPage;
import com.interview_master.dto.SortOrder;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CollectionRepositoryCustom {

  public Page<CollectionWithAttempts> searchCollections(List<Long> categoryIds,
      List<String> keywords,
      Integer maxCorrectRate, Pageable pageable, Long userId);

}
