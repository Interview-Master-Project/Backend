package com.interview_master.infrastructure;

import java.util.Optional;

import com.interview_master.domain.quiz.Quiz;
import org.springframework.data.repository.Repository;

public interface QuizRepository extends Repository<Quiz, Long> {
    Optional<Quiz> findById(Long id);

    void save(Quiz quiz);
}
