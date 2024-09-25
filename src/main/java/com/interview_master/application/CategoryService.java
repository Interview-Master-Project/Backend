package com.interview_master.application;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.infrastructure.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public void areAllCategoriesExist(List<Long> categoryIds) {
    if (categoryIds == null || categoryIds.isEmpty()) {
      return;
    }

    Integer exisitingCount = categoryRepository.countByIdIn(categoryIds);

    if (exisitingCount != categoryIds.size()) {
      throw new ApiException(ErrorCode.CATEGORY_NOT_FOUND, "존재하지 않는 카테고리가 있습니다.");
    }
  }
}
