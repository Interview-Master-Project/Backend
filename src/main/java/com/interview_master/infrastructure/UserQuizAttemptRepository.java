package com.interview_master.infrastructure;

import com.interview_master.domain.userquizattempt.UserQuizAttempt;
import com.interview_master.dto.QuizLog;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

  @Query("select uqa from UserQuizAttempt uqa "
      + "where uqa.quiz.id = :quizId "
      + "and uqa.user.id = :userId "
      + "and uqa.quiz.isDeleted = false "
      + "and uqa.quiz.collection.isDeleted = false "
      + "order by uqa.answeredAt desc")
  List<UserQuizAttempt> findAllByQuizIdAndUserIdOrderByAnsweredAtDesc(@Param("quizId") Long quizId,
      @Param("userId") Long userId);


  @Query("SELECT uqa FROM UserQuizAttempt uqa " +
      "WHERE uqa.collectionAttempt.id = :collectionAttemptId " +
      "AND uqa.user.id = :userId " +
      "AND uqa.quiz.isDeleted = false " +
      "AND uqa.quiz.collection.isDeleted = false " +
      "ORDER BY uqa.quiz.id")
  List<UserQuizAttempt> findAllByCollectionAttemptIdAndUserIdOrderByQuizId(
      @Param("collectionAttemptId") Long collectionAttemptId,
      @Param("userId") Long userId
  );

  @Modifying
  @Query("delete from UserQuizAttempt uqa "
      + "where uqa.collectionAttempt.collection.id in :collectionIds")
  int deleteAllByCollectionIdIn(@Param("collectionIds") List<Long> collectionIds);

  int deleteAllByQuizIdIn(List<Long> quizIds);

  /**
   * 유저 탈퇴로 인한 시도 기록 익명 처리
   */
  @Modifying
  @Query("update UserQuizAttempt qa set qa.user.id = 0L where qa.user.id in :userIds")
  int anonymizedByUserIdIn(@Param("userIds") List<Long> userIds);

  // 탈퇴유저 삭제 스케줄러 테스트를 위한 쿼리
  List<UserQuizAttempt> findByUserId(Long userId);

  /**
   * 퀴즈 삭제 후 컬렉션 시도 기록 수정을 위한 조회
   */
  List<UserQuizAttempt> findAllByQuizId(Long quizId);

  @Modifying
  @Query("delete from UserQuizAttempt uqa where uqa.collectionAttempt.id = :dcaId")
  int deleteAllByCollectionAttemptId(@Param("dcaId") Long dCollectionAttemptId);
}
