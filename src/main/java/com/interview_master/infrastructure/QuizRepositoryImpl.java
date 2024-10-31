package com.interview_master.infrastructure;

import static com.interview_master.common.constant.Constant.SORT_LOWACCURACY;
import static com.interview_master.domain.quiz.QQuiz.quiz;
import static com.interview_master.domain.userquizattempt.QUserQuizAttempt.userQuizAttempt;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.dto.QuizWithAttempt;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class QuizRepositoryImpl implements QuizRepositoryCustom {

  private final JPQLQueryFactory queryFactory;

  public QuizRepositoryImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }


  @Override
  public Page<QuizWithAttempt> searchQuizzes(List<Long> categoryIds, List<String> keywords,
      Integer maxCorrectRate, Pageable pageable, Long userId) {

    if (userId == null) {
      throw new ApiException(ErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
    }

    BooleanBuilder whereClause = new BooleanBuilder();

    whereClause.and(quiz.isDeleted.eq(false));

    if (categoryIds != null && !categoryIds.isEmpty()) {
      whereClause.and(quiz.collection.category.id.in(categoryIds));
    }

    if (keywords != null && !keywords.isEmpty()) {
      BooleanBuilder keywordCondition = new BooleanBuilder();
      for (String keyword : keywords) {
        keywordCondition.or(quiz.question.containsIgnoreCase(keyword));
      }
      whereClause.and(keywordCondition);
    }

    JPQLQuery<QuizWithAttempt> query = queryFactory
        .select(Projections.constructor(QuizWithAttempt.class,
            quiz,
            userQuizAttempt.count().coalesce(0L),
            userQuizAttempt.isCorrect.when(true).then(1L).otherwise(0L).sum().coalesce(0L),
            userQuizAttempt.answeredAt.max()
        ))
        .from(quiz)
        .leftJoin(userQuizAttempt).on(userQuizAttempt.quiz.eq(quiz)
            .and(userQuizAttempt.user.id.eq(userId)))
        .where(quiz.creator.id.eq(userId));

    query.where(whereClause);


    if (maxCorrectRate != null) {
      query.having(userQuizAttempt.count().eq(0L)
          .or(Expressions.booleanTemplate(
              "CASE WHEN COUNT({0}) = 0 THEN true " +
                  "ELSE (100.0 * SUM(CASE WHEN {1} = true THEN 1 ELSE 0 END)) / COUNT({0}) <= {2} END",
              userQuizAttempt.id,
              userQuizAttempt.isCorrect,
              maxCorrectRate)));
    }

    query.groupBy(quiz);

    // 정렬 처리
    Sort.Order accuracyOrder = pageable.getSort().getOrderFor(SORT_LOWACCURACY);
    if (accuracyOrder != null) {
      // 정답률 계산 표현식
      NumberTemplate<Double> accuracyExpression = Expressions.numberTemplate(Double.class,
          "CASE WHEN COUNT({0}) = 0 THEN 0.0 " +
              "ELSE (100.0 * SUM(CASE WHEN {1} = true THEN 1 ELSE 0 END)) / COUNT({0}) END",
          userQuizAttempt.id,
          userQuizAttempt.isCorrect
      );

      if (accuracyOrder.isAscending()) {
        query.orderBy(
            accuracyExpression.asc(),
            userQuizAttempt.count().desc(),
            userQuizAttempt.answeredAt.max().coalesce(quiz.createdAt).desc()
        );
      } else {
        query.orderBy(
            accuracyExpression.desc(),
            userQuizAttempt.count().desc(),
            userQuizAttempt.answeredAt.max().coalesce(quiz.createdAt).desc()
        );
      }
    } else {
      // 최신순 정렬
      query.orderBy(quiz.createdAt.desc());
    }

    long total = query.fetchCount();
    query.offset(pageable.getOffset()).limit(pageable.getPageSize());
    List<QuizWithAttempt> content = query.fetch();

    int pageNumber = (int) (pageable.getOffset() / pageable.getPageSize());
    return new PageImpl<>(content, PageRequest.of(pageNumber, pageable.getPageSize()), total);
  }
}
