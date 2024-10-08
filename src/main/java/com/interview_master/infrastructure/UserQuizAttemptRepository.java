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
}
