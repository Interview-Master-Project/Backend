package com.interview_master.dto;

import com.interview_master.domain.collection.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionWithAttempt {

  private Collection collection;
  private int quizCount;
  private int totalAttempts;
  private int totalCorrectAttempts;
  private int recentAttempts;
  private int recentCorrectAttempts;

  // CollectionRepositoryImpl에서 필요한 생성자
  public CollectionWithAttempt(Collection collection, int quizCount, int recentAttempts, int recentCorrectAttempts) {
    this.collection = collection;
    this.quizCount = quizCount;
    this.recentAttempts = recentAttempts;
    this.recentCorrectAttempts = recentCorrectAttempts;
  }

}
