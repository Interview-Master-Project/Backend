package com.interview_master.infrastructure;

import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CollectionRepositoryImpl implements CollectionRepositoryCustom {

  private final JPQLQueryFactory queryFactory;



}
