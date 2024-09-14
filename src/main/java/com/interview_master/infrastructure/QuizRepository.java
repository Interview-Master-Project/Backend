package com.interview_master.infrastructure;

import com.interview_master.domain.quiz.Quiz;
import com.interview_master.dto.QuizWithAttempts;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends Repository<Quiz, Long> {

    void save(Quiz quiz);

//    @Query("select new com.interview_master.dto.QuizWithAttempts(q, count(uqa), " +
//            "sum(case when uqa.isCorrect = true then 1 else 0 end), max(uqa.answeredAt))" +
//            "from Quiz q" +
//            "left join UserQuizAttempt uqa on q = uqa.quiz and qua.user.id = :userId " +
//            "where q.collection.id = :collectionId " +
//            "and q.isDelete = false " +
//            "group by q.id " +
//            "order by q.id desc")
//    List<QuizWithAttempts> getQuizzesByCollectionIdWithAttempts(@Param("collectionId") Long collectionId, @Param("userId") Long userId);

    @Query("SELECT NEW com.interview_master.dto.QuizWithAttempts(q, COUNT(uqa), " +
            "SUM(CASE WHEN uqa.isCorrect = true THEN 1 ELSE 0 END) , MAX(uqa.answeredAt)) " +
            "FROM Quiz q " +
            "LEFT JOIN UserQuizAttempt uqa ON q = uqa.quiz AND uqa.user.id = :userId " +
            "WHERE q.collection.id = :collectionId " +
            "AND q.isDeleted = false " +
            "GROUP BY q.id " +  // 필요한 필드들을 추가
            "ORDER BY q.id DESC")
    List<QuizWithAttempts> getQuizzesByCollectionIdWithAttempts(
            @Param("collectionId") Long collectionId,
            @Param("userId") Long userId
    );
}
