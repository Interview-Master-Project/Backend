package com.interview_master.infrastructure;

import static com.interview_master.domain.collection.QCollection.collection;
import static com.interview_master.domain.usercollectionattempt.QUserCollectionAttempt.userCollectionAttempt;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.Access;
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

    JPQLQuery<CollectionWithAttempt> query;

    query = queryFactory
        .select(Projections.constructor(CollectionWithAttempt.class,
            collection,
            collection.quizzes.size(),
            userCollectionAttempt.totalQuizCount,
            userCollectionAttempt.correctQuizCount))
        .from(collection)
        .leftJoin(userCollectionAttempt)
        .on(userCollectionAttempt.collection.eq(collection)
            .and(userCollectionAttempt.user.id.eq(userId))
            .and(userCollectionAttempt.completedAt.isNotNull())
            .and(userCollectionAttempt.completedAt.eq(
                JPAExpressions.select(userCollectionAttempt.completedAt.max())
                    .from(userCollectionAttempt)
                    .where(userCollectionAttempt.collection.eq(collection)
                        .and(userCollectionAttempt.user.id.eq(userId))
                        .and(userCollectionAttempt.completedAt.isNotNull()))
            )))
        .where(collection.access.eq(Access.PUBLIC).or(collection.creator.id.eq(userId)));

    // Max correct rate (정답률 x% 이하 조건)
    NumberExpression<Integer> accuracyExpression = userCollectionAttempt.correctQuizCount
        .multiply(100.0)
        .divide(userCollectionAttempt.totalQuizCount.coalesce(1));

    if (maxCorrectRate != null) {
      whereClause
          .and(userCollectionAttempt.completedAt.isNotNull()) // 정상적으로 완료되고
          .and(userCollectionAttempt.totalQuizCount.gt(0) // 실제 문제 풀었던 기록에 대해서
              .and(accuracyExpression.loe(maxCorrectRate)));
    }

    query.where(whereClause);

    // LOWEST_ACCURACY sorting
    if (pageable.getSort().getOrderFor("accuracy") != null) {
      accuracyExpression =
          userCollectionAttempt.correctQuizCount.multiply(100)
              .divide(userCollectionAttempt.totalQuizCount.coalesce(1));

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
