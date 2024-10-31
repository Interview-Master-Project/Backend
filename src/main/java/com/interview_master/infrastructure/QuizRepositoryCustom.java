package com.interview_master.infrastructure;

import com.interview_master.dto.QuizWithAttempt;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuizRepositoryCustom {

  Page<QuizWithAttempt> searchQuizzes(List<Long> categoryIds, List<String> keywords,
      Integer maxCorrectRate, Pageable pageable, Long userId);

}
