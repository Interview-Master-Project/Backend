package com.interview_master.infrastructure;

import static com.interview_master.domain.collection.QCollection.collection;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.Access;
import com.interview_master.domain.usercollectionattempt.QUserCollectionAttempt;
import com.interview_master.dto.CollectionWithAttempt;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class CollectionRepositoryImpl implements CollectionRepositoryCustom {

  private final JPQLQueryFactory queryFactory;

  public CollectionRepositoryImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Page<CollectionWithAttempt> searchCollectionsForAuthUser(List<Long> categoryIds,
      List<String> keywords, Integer maxCorrectRate, Pageable pageable, Long userId) {
    if (userId == null) {
      throw new ApiException(ErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
    }

    BooleanBuilder whereClause = new BooleanBuilder();

    whereClause.and(collection.isDeleted.eq(false));

    // Category IDs
    if (categoryIds != null && !categoryIds.isEmpty()) {
      whereClause.and(collection.category.id.in(categoryIds));
    }

    // Keywords
    if (keywords != null && !keywords.isEmpty()) {
      BooleanBuilder keywordCondition = new BooleanBuilder();
      for (String keyword : keywords) {
        keywordCondition.or(collection.name.containsIgnoreCase(keyword)
            .or(collection.description.containsIgnoreCase(keyword)));
      }
      whereClause.and(keywordCondition);
    }

    QUserCollectionAttempt recentAttempt = new QUserCollectionAttempt("recentAttempt");
    QUserCollectionAttempt totalAttempt = new QUserCollectionAttempt("totalAttempt");

    JPQLQuery<CollectionWithAttempt> query;

    query = queryFactory
        .select(Projections.constructor(CollectionWithAttempt.class,
            collection,
            collection.quizzes.size(),
            totalAttempt.totalQuizCount.sum(),
            totalAttempt.correctQuizCount.sum(),
            recentAttempt.totalQuizCount,
            recentAttempt.correctQuizCount))
        .from(collection)
        .leftJoin(recentAttempt)
        .on(recentAttempt.collection.eq(collection)
            .and(recentAttempt.user.id.eq(userId))
            .and(recentAttempt.completedAt.isNotNull())
            .and(recentAttempt.completedAt.eq(
                JPAExpressions.select(recentAttempt.completedAt.max())
                    .from(recentAttempt)
                    .where(recentAttempt.collection.eq(collection)
                        .and(recentAttempt.user.id.eq(userId))
                        .and(recentAttempt.completedAt.isNotNull()))
            )))
        .leftJoin(totalAttempt)
        .on(totalAttempt.collection.eq(collection)
            .and(totalAttempt.completedAt.isNotNull())
        )
        .where(collection.access.eq(Access.PUBLIC).or(collection.creator.id.eq(userId)))
        .groupBy(collection, collection.quizzes.size(), recentAttempt.totalQuizCount,
            recentAttempt.correctQuizCount);

    // Max correct rate (정답률 x% 이하 조건)
    NumberExpression<Integer> accuracyExpression = recentAttempt.correctQuizCount
        .multiply(100.0)
        .divide(recentAttempt.totalQuizCount.coalesce(1));

    if (maxCorrectRate != null) {
      whereClause
          .and(recentAttempt.completedAt.isNotNull()) // 정상적으로 완료되고
          .and(recentAttempt.totalQuizCount.gt(0) // 실제 문제 풀었던 기록에 대해서
              .and(accuracyExpression.loe(maxCorrectRate)));
    }

    query.where(whereClause);

    // LOWEST_ACCURACY sorting
    if (pageable.getSort().getOrderFor("accuracy") != null) {
      accuracyExpression =
          recentAttempt.correctQuizCount.multiply(100)
              .divide(recentAttempt.totalQuizCount.coalesce(1));

      if (pageable.getSort().getOrderFor("accuracy").isAscending()) {
        query.orderBy(accuracyExpression.asc());
      } else {
        query.orderBy(accuracyExpression.desc());
      }
    } else {
      // LATEST sorting
      query.orderBy(collection.createdAt.desc());
    }

    long total = query.fetchCount();

    // Apply pagination
    query.offset(pageable.getOffset()).limit(pageable.getPageSize());
    List<CollectionWithAttempt> content = query.fetch();

    int pageNumber = (int) (pageable.getOffset() / pageable.getPageSize());
    return new PageImpl<>(content, PageRequest.of(pageNumber, pageable.getPageSize()), total);
  }
}
