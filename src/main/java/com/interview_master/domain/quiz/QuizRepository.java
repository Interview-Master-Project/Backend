package com.interview_master.domain.quiz;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface QuizRepository extends Repository<Quiz, Long> {
    Optional<Quiz> findById(Long id);

    void save(Quiz quiz);
}
