package com.interview_master.service;

import static com.interview_master.util.PageSortUtils.createPageable;
import static com.interview_master.util.PageSortUtils.createSort;

import com.interview_master.dto.DataPage;
import com.interview_master.dto.QuizWithAttempt;
import com.interview_master.dto.SortOrder;
import com.interview_master.infrastructure.QuizRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchQuizService {

  private final CategoryService categoryService;
  private final QuizRepository quizRepository;

  public Page<QuizWithAttempt> searchQuizzes(List<Long> categoryIds, List<String> keywords,
      Integer maxCorrectRate, DataPage paging, SortOrder sortOrder, Long userId) {
    categoryService.areAllCategoriesExist(categoryIds);

    Sort sort = createSort(sortOrder);
    Pageable pageable = createPageable(paging, sort);


    return quizRepository.searchQuizzes(categoryIds, keywords, maxCorrectRate, pageable, userId);
  }

}
