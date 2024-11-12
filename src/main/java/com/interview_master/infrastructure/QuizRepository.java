package com.interview_master.infrastructure;

import com.interview_master.domain.quiz.Quiz;
import com.interview_master.dto.QuizWithAttempt;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends Repository<Quiz, Long>, QuizRepositoryCustom{

  Optional<Quiz> findByIdAndIsDeletedFalse(Long quizId);

  Quiz save(Quiz quiz);

  @Query("SELECT NEW com.interview_master.dto.QuizWithAttempt(q, " +
      "COALESCE(COUNT(uqa), 0), " +
      "COALESCE(SUM(CASE WHEN uqa.isCorrect = true THEN 1 ELSE 0 END), 0), " +
      "MAX(uqa.answeredAt)) " +
      "FROM Quiz q " +
      "LEFT JOIN UserQuizAttempt uqa ON q.id = uqa.quiz.id AND uqa.user.id = :userId " +
      "WHERE q.collection.id = :collectionId "
      + "AND q.collection.isDeleted = false "
      + "AND q.isDeleted = false " +
      "GROUP BY q.id, q.question " +
      "ORDER BY q.id DESC")
  List<QuizWithAttempt> getQuizzesByCollectionIdWithAttempts(
      @Param("collectionId") Long collectionId,
      @Param("userId") Long userId
  );

  @Query("select q.id from Quiz q where q.isDeleted = true")
  List<Long> findIdsByIsDeletedTrue();

  /**
   * user들이 생성한 퀴즈들 모두 삭제
   */
  int deleteByCreatorIdIn(List<Long> userIds);

  int deleteAllByIdIn(List<Long> quizIds);

  /**
   * 컬렉션들에 속한 퀴즈들 모두 삭제
   */
  int deleteAllByCollectionIdIn(List<Long> collectionIds);

  // deleteUserScheduler 테스트 용 쿼리
  List<Quiz> findByCreatorId(Long userId);

  @Modifying
  @Query("update Quiz q set q.isDeleted = true where q.creator.id = :userId")
  int softDeleteAllByUserId(@Param("userId") Long userId);
}
