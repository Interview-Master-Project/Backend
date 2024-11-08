package com.interview_master.infrastructure;

import com.interview_master.domain.userquizattempt.UserQuizAttempt;
import com.interview_master.dto.QuizLog;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserQuizAttemptRepository extends JpaRepository<UserQuizAttempt, Long> {

  @Query(value =
      "WITH date_range AS (" +
          "    SELECT generate_series(:startDate, :endDate, '1 day'::interval)::date AS date" +
          ")" +
          "SELECT " +
          "    dr.date AS date, " +
          "    COALESCE(COUNT(uqa.id), 0) AS quizzes_solved " +
          "FROM " +
          "    date_range dr " +
          "LEFT JOIN user_quiz_attempts uqa ON " +
          "    dr.date = DATE(uqa.answered_at) AND uqa.user_id = :userId " +
          "GROUP BY " +
          "    dr.date " +
          "ORDER BY " +
          "    dr.date",
      nativeQuery = true)
  List<QuizLog> findDailyQuizCounts(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("userId") Long userId
  );

  // collectionAttemptId와 userId에 해당하는 퀴즈 기록들 중에서 맞은 퀴즈 개수만 가져오기
  @Query("select count(*) from UserQuizAttempt "
      + "where collectionAttempt.id = :ucaId "
      + "and user.id = :userId "
      + "and isCorrect = true")
  int countCorrectAttempts(@Param("ucaId") Long collectionAttemptId, @Param("userId") Long userId);

  List<UserQuizAttempt> findAllByQuizIdAndUserIdOrderByAnsweredAtDesc(Long quizId, Long userId);

  List<UserQuizAttempt> findAllByCollectionAttemptIdAndUserIdOrderByQuizId(Long collectionAttemptId,
      Long userId);
}
