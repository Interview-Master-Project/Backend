package com.interview_master.infrastructure;

import static com.interview_master.common.constant.Constant.SORT_LOWACCURACY;
import static com.interview_master.common.constant.Constant.SORT_MOSTLIKED;
import static com.interview_master.domain.collection.QCollection.collection;
import static com.interview_master.domain.collectionlike.QCollectionsLikes.collectionsLikes;
import static com.interview_master.domain.quiz.QQuiz.quiz;
import static com.interview_master.domain.usercollectionattempt.QUserCollectionAttempt.userCollectionAttempt;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.Access;
import com.interview_master.domain.usercollectionattempt.QUserCollectionAttempt;
import com.interview_master.dto.CollectionWithAttempt;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
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
  public Page<CollectionWithAttempt> searchCollectionsForGuest(List<Long> categoryIds,
      List<String> keywords, Pageable pageable) {

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

    JPQLQuery<CollectionWithAttempt> query = queryFactory
        .select(Projections.constructor(CollectionWithAttempt.class,
            collection,
            quiz.id.count().intValue(),
            // 전체 시도 횟수와 정답 횟수는 모든 사용자의 기록을 집계
            JPAExpressions
                .select(userCollectionAttempt.totalQuizCount.sum().coalesce(0))
                .from(userCollectionAttempt)
                .where(userCollectionAttempt.collection.eq(collection)
                    .and(userCollectionAttempt.completedAt.isNotNull())),
            JPAExpressions
                .select(userCollectionAttempt.correctQuizCount.sum().coalesce(0))
                .from(userCollectionAttempt)
                .where(userCollectionAttempt.collection.eq(collection)
                    .and(userCollectionAttempt.completedAt.isNotNull())),
            Expressions.constant(0),  // recentAttempts
            Expressions.constant(0))) // recentCorrectAttempts
        .from(collection)

        .leftJoin(collection.quizzes, quiz)
        .on(quiz.isDeleted.eq(false))  // isDeleted = false인 퀴즈만 join

        .where(whereClause)
        .where(collection.access.eq(Access.PUBLIC))
        .groupBy(collection)
        .orderBy(collection.likes.desc(),
            collection.createdAt.desc()); // likes 수 기준 내림차순 정렬 -> likes 수 같으면 최신순

    long total = query.fetchCount();

    // Apply pagination
    query.offset(pageable.getOffset()).limit(pageable.getPageSize());
    List<CollectionWithAttempt> content = query.fetch();

    int pageNumber = (int) (pageable.getOffset() / pageable.getPageSize());
    return new PageImpl<>(content, PageRequest.of(pageNumber, pageable.getPageSize()), total);
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
            quiz.id.count().intValue(),
            JPAExpressions
                .select(totalAttempt.totalQuizCount.sum().coalesce(0))
                .from(totalAttempt)
                .where(totalAttempt.collection.eq(collection)
                    .and(totalAttempt.completedAt.isNotNull())),
            JPAExpressions
                .select(totalAttempt.correctQuizCount.sum().coalesce(0))
                .from(totalAttempt)
                .where(totalAttempt.collection.eq(collection)
                    .and(totalAttempt.completedAt.isNotNull())),
            recentAttempt.totalQuizCount,
            recentAttempt.correctQuizCount,
            collectionsLikes.id.isNotNull()))
        .from(collection)

        .leftJoin(collection.quizzes, quiz)
        .on(quiz.isDeleted.eq(false))  // isDeleted = false인 퀴즈만 join

        .leftJoin(collectionsLikes)
        .on(collectionsLikes.collection.eq(collection)
            .and(collectionsLikes.user.id.eq(userId)))

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
        .where(collection.access.eq(Access.PUBLIC).or(collection.creator.id.eq(userId)))
        .groupBy(collection, collectionsLikes.id, recentAttempt.totalQuizCount, recentAttempt.correctQuizCount);

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
    if (pageable.getSort().getOrderFor(SORT_LOWACCURACY) != null) {
      accuracyExpression =
          recentAttempt.correctQuizCount.multiply(100)
              .divide(recentAttempt.totalQuizCount.coalesce(1));

      if (pageable.getSort().getOrderFor(SORT_LOWACCURACY).isAscending()) {
        query.orderBy(accuracyExpression.asc());
      } else {
        query.orderBy(accuracyExpression.desc());
      }
    } else if (pageable.getSort().getOrderFor(SORT_MOSTLIKED) != null) {
      query.orderBy(
          collection.likes.desc(),
          collection.createdAt.desc()
      );
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

  @Override
  public Page<CollectionWithAttempt> myCollections(Long userId, Pageable pageable) {
    if (userId == null) {
      throw new ApiException(ErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
    }

    BooleanBuilder whereClause = new BooleanBuilder();
    whereClause.and(collection.isDeleted.eq(false));

    QUserCollectionAttempt totalAttempt = new QUserCollectionAttempt("totalAttempt");
    // 정확도 계산을 위한 서브쿼리
    NumberExpression<Integer> accuracySubQuery = Expressions.numberTemplate(Integer.class,
        "(SELECT COALESCE(SUM(totalAttempt.correctQuizCount) * 100 / NULLIF(SUM(totalAttempt.totalQuizCount), 0), 0) " +
            "FROM UserCollectionAttempt totalAttempt " +
            "WHERE totalAttempt.collection = {0} " +
            "AND totalAttempt.user.id = {1} " +
            "AND totalAttempt.completedAt IS NOT NULL)",
        collection, userId);


    JPQLQuery<CollectionWithAttempt> query;
    query = queryFactory
        .select(Projections.constructor(CollectionWithAttempt.class,
            collection,
            quiz.id.count().intValue(),
            JPAExpressions
                .select(totalAttempt.totalQuizCount.sum().coalesce(0))
                .from(totalAttempt)
                .where(totalAttempt.collection.eq(collection)
                    .and(totalAttempt.user.id.eq(userId))
                    .and(totalAttempt.completedAt.isNotNull())),
            JPAExpressions
                .select(totalAttempt.correctQuizCount.sum().coalesce(0))
                .from(totalAttempt)
                .where(totalAttempt.collection.eq(collection)
                    .and(totalAttempt.user.id.eq(userId))
                    .and(totalAttempt.completedAt.isNotNull())),
            Expressions.constant(0),
            Expressions.constant(0),
            collectionsLikes.id.isNotNull()))
        .from(collection)

        .leftJoin(collection.quizzes, quiz)
        .on(quiz.isDeleted.eq(false))  // isDeleted = false인 퀴즈만 join

        .leftJoin(collectionsLikes)
        .on(collectionsLikes.collection.eq(collection)
            .and(collectionsLikes.user.id.eq(userId)))

        .where(collection.creator.id.eq(userId))
        .groupBy(collection, collectionsLikes.id);

    query.where(whereClause);

    NumberExpression<Integer> accuracyExpression;
    // LOWEST_ACCURACY sorting
    if (pageable.getSort().getOrderFor(SORT_LOWACCURACY) != null) {
      if (pageable.getSort().getOrderFor(SORT_LOWACCURACY).isAscending()) {
        query.orderBy(accuracySubQuery.coalesce(0).asc(), collection.createdAt.desc());
      } else {
        query.orderBy(accuracySubQuery.coalesce(0).desc(), collection.createdAt.desc());
      }
    } else if (pageable.getSort().getOrderFor(SORT_MOSTLIKED) != null) {
      query.orderBy(
          collection.likes.desc(),
          collection.createdAt.desc()
      );
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
