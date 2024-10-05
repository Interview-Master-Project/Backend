package com.interview_master.infrastructure;

import com.interview_master.domain.quiz.Quiz;
import com.interview_master.dto.QuizWithAttempt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends Repository<Quiz, Long> {

  Optional<Quiz> findByIdAndIsDeletedFalse(Long quizId);

  Quiz save(Quiz quiz);

  @Query("SELECT NEW com.interview_master.dto.QuizWithAttempt(q, " +
      "COALESCE(COUNT(uqa), 0), " +
      "COALESCE(SUM(CASE WHEN uqa.isCorrect = true THEN 1 ELSE 0 END), 0), " +
      "MAX(uqa.answeredAt)) " +
      "FROM Quiz q " +
      "LEFT JOIN UserQuizAttempt uqa ON q.id = uqa.quiz.id AND uqa.user.id = :userId " +
      "WHERE q.collection.id = :collectionId AND q.isDeleted = false " +
      "GROUP BY q.id, q.question " +
      "ORDER BY q.id DESC")
  List<QuizWithAttempt> getQuizzesByCollectionIdWithAttempts(
      @Param("collectionId") Long collectionId,
      @Param("userId") Long userId
  );
}
