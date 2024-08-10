package com.interview_master.infrastructure;

import com.interview_master.domain.quizresult.QuizResult;
import org.springframework.data.repository.Repository;

public interface QuizResultRepository extends Repository<QuizResult, Long> {

    void save(QuizResult quizResult);
}
