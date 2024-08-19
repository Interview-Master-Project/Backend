package com.interview_master.infrastructure;

import com.interview_master.domain.quiz.Quiz;
import com.interview_master.ui.response.QuizWithCollectionAndResults;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.graphql.data.GraphQlRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@GraphQlRepository
public interface QuizRepository extends Repository<Quiz, Long> {

    void save(Quiz quiz);

    Optional<Quiz> findById(Long id);

    /**
     * 퀴즈 생성자면 그냥 가져오고 생성자가 아니면 PUBLIC인 퀴즈만 가져오기
     */
    @Query("SELECT q FROM Quiz q " +
            "WHERE q.isDeleted = false AND q.id = :id AND " +
            "(q.creatorId = :userId OR q.access = 'PUBLIC')")
    Optional<Quiz> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 내 질문 목록 가져오기( quiz + collection + quizResult 모두 조인해서)
     */
    @Query("SELECT new com.interview_master.ui.response.QuizWithCollectionAndResults(q, c, qr) " +
            "FROM Quiz q " +
            "JOIN Collection c ON q.collectionId = c.id " +
            "LEFT JOIN QuizResult qr ON qr.quizId = q.id AND qr.userId = :creatorId " +
            "WHERE q.isDeleted = false AND q.creatorId = :creatorId " +
            "ORDER BY q.id DESC ")
    List<QuizWithCollectionAndResults> findQuizzesWithCollectionNameAndResults(@Param("creatorId") Long creatorId);


    /**
     * collectionId에 해당하는 질문 목록 가져오기
     */
    @Query("SELECT new com.interview_master.ui.response.QuizWithCollectionAndResults(q, c, qr) " +
            "FROM Quiz q " +
            "JOIN Collection c ON q.collectionId = c.id " +
            "LEFT JOIN QuizResult qr ON qr.quizId = q.id " +
            "WHERE q.collectionId = :collectionId AND c.isDeleted = false AND q.isDeleted = false " +
            "ORDER BY q.id DESC ")
    List<QuizWithCollectionAndResults> findByCollectionId(@Param("collectionId") Long collectionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Quiz q WHERE q.isDeleted = true")
    void deleteAllByIsDeletedTrueDirect();
}
