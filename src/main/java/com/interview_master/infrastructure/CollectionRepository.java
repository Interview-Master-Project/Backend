package com.interview_master.infrastructure;

import com.interview_master.domain.collection.Collection;

import java.util.List;
import java.util.Optional;

import com.interview_master.ui.response.CollectionWithCategoryAndQuizCountAndResults;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface CollectionRepository extends Repository<Collection, Long> {

    Optional<Collection> findById(Long id);

    /**
     * collection 정보 + category 정보 + 해당 컬렉션의 quiz 개수 + 퀴즈들의 맞은 개수, 틀린 개수 가져오기
     */
    @Query("select new com.interview_master.ui.response.CollectionWithCategoryAndQuizCountAndResults(" +
            "(select count(distinct q.id) from Quiz q where q.collectionId = c.id), sum(qr.correctAttempts), sum(qr.wrongAttempts), c, cg) from Collection c " +
            "join Category cg on c.categoryId = cg.id " +
            "left join Quiz q on c.id = q.collectionId " +
            "left join QuizResult qr on q.id = qr.quizId and qr.userId = :creatorId " +
            "where c.isDeleted = false and c.creatorId = :creatorId " +
            "group by c.id, cg.id " +
            "order by c.id desc")
    List<CollectionWithCategoryAndQuizCountAndResults> findCollectionsByUserId(@Param("creatorId") Long creatorId);
}
