package com.interview_master.infrastructure;

import java.util.List;
import java.util.Optional;

import com.interview_master.domain.quiz.Quiz;
import org.springframework.data.repository.Repository;
import org.springframework.graphql.data.GraphQlRepository;

@GraphQlRepository
public interface QuizRepository extends Repository<Quiz, Long> {
    Optional<Quiz> findById(Long id);

    void save(Quiz quiz);

    List<Quiz> findByCreatorIdOrderByIdDesc(Long creatorId);
}
