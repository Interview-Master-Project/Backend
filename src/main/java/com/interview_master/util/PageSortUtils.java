package com.interview_master.util;

import static com.interview_master.common.constant.Constant.SORT_LATEST;
import static com.interview_master.common.constant.Constant.SORT_LOWACCURACY;

import com.interview_master.dto.DataPage;
import com.interview_master.dto.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageSortUtils {

  public static Sort createSort(SortOrder sortOrder) {
    return switch (sortOrder) {
      case LATEST -> Sort.by(Sort.Direction.DESC, SORT_LATEST);
      case LOWEST_ACCURACY -> Sort.by(Sort.Direction.ASC, SORT_LOWACCURACY);
    };
  }

  public static Pageable createPageable(DataPage dataPage, Sort sort) {
    int pageNumber = dataPage.getOffset() / dataPage.getPageSize();
    return PageRequest.of(pageNumber, dataPage.getPageSize(), sort);
  }

  public static Pageable createPageable(DataPage dataPage) {
    int pageNumber = dataPage.getOffset() / dataPage.getPageSize();
    return PageRequest.of(pageNumber, dataPage.getPageSize());
  }

}
