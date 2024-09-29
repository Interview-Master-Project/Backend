package com.interview_master.infrastructure;

import static com.interview_master.domain.collection.QCollection.collection;
import static com.interview_master.domain.usercollectionattempt.QUserCollectionAttempt.userCollectionAttempt;

import com.interview_master.domain.Access;
import com.interview_master.dto.CollectionWithAttempts;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
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
  public Page<CollectionWithAttempts> searchCollections(List<Long> categoryIds,
      List<String> keywords, Integer maxCorrectRate, Pageable pageable, Long userId) {
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

    JPQLQuery<CollectionWithAttempts> query;

    if (userId != null) {
      // 로그인한 사용자를 위한 쿼리
      query = queryFactory
          .select(Projections.constructor(CollectionWithAttempts.class,
              collection,
              collection.quizzes.size(),
              userCollectionAttempt.totalQuizCount.sum().coalesce(0).as("totalAttempts"),
              userCollectionAttempt.correctQuizCount.sum().coalesce(0).as("correctAttempts")))
          .from(collection)
          .leftJoin(userCollectionAttempt)
          .on(userCollectionAttempt.collection.eq(collection)
              .and(userCollectionAttempt.user.id.eq(userId)))
          .where(collection.access.eq(Access.PUBLIC).or(collection.creator.id.eq(userId)));
    } else {
      // 비로그인 사용자를 위한 쿼리
      query = queryFactory
          .select(Projections.constructor(CollectionWithAttempts.class,
              collection,
              collection.quizzes.size(),
              Expressions.constant(0),
              Expressions.constant(0)))
          .from(collection)
          .where(collection.access.eq(Access.PUBLIC));
    }

    query.where(whereClause);

    if (userId != null) {
      query.groupBy(collection);

      // Max correct rate (로그인 사용자에게만 적용)
      if (maxCorrectRate != null) {
        query.having(userCollectionAttempt.totalQuizCount.sum().coalesce(0).eq(0)
            .or(userCollectionAttempt.correctQuizCount.sum().multiply(100.0)
                .divide(userCollectionAttempt.totalQuizCount.sum())
                .loe(maxCorrectRate)));
      }
    }

    // Apply sorting
    if (pageable.getSort().getOrderFor("accuracy") != null && userId != null) {
      // LOWEST_ACCURACY sorting (로그인 사용자에게만 적용)
      query.orderBy(userCollectionAttempt.correctQuizCount.sum().multiply(100.0)
          .divide(userCollectionAttempt.totalQuizCount.sum().coalesce(1))
          .as("accuracy")
          .asc());
    } else {
      // LATEST sorting
      query.orderBy(collection.createdAt.desc());
    }

    long total = query.fetchCount();

    // Apply pagination
    query.offset(pageable.getOffset()).limit(pageable.getPageSize());
    List<CollectionWithAttempts> content = query.fetch();

    int pageNumber = (int) (pageable.getOffset() / pageable.getPageSize());
    return new PageImpl<>(content, PageRequest.of(pageNumber, pageable.getPageSize()), total);
  }
}
