package com.interview_master.infrastructure;

import java.util.List;
import java.util.Optional;

import com.interview_master.domain.quiz.Quiz;
import com.interview_master.ui.response.QuizWithCollectionNameAndResults;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.graphql.data.GraphQlRepository;

@GraphQlRepository
public interface QuizRepository extends Repository<Quiz, Long> {

    /**
     * 퀴즈 생성자면 그냥 가져오고 생성자가 아니면 PUBLIC인 퀴즈만 가져오기
     */
    @Query("SELECT q FROM Quiz q " +
        "WHERE q.isDeleted = false AND q.id = :id AND " +
        "(q.creatorId = :userId OR q.access = 'PUBLIC')")
    Optional<Quiz> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    void save(Quiz quiz);

    @Query("SELECT new com.interview_master.ui.response.QuizWithCollectionNameAndResults(q, c.name, qr.correctAttempts, qr.wrongAttempts) " +
            "FROM Quiz q " +
            "JOIN Collection c ON q.collectionId = c.id " +
            "LEFT JOIN QuizResult qr ON qr.quizId = q.id AND qr.userId = :creatorId " +
            "WHERE q.isDeleted = false AND q.creatorId = :creatorId")
    List<QuizWithCollectionNameAndResults> findQuizzesWithCollectionNameAndResults(@Param("creatorId") Long creatorId);

}
