package com.interview_master.infrastructure;

import com.interview_master.domain.quiz.Quiz;
import org.springframework.data.repository.Repository;

public interface QuizRepository extends Repository<Quiz, Long> {

    void save(Quiz quiz);
}
