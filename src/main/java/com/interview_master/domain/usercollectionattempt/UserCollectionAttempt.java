package com.interview_master.domain.usercollectionattempt;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_collection_attempts")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCollectionAttempt {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "collection_id")
  private Collection collection;

  private LocalDateTime startedAt;

  private LocalDateTime completedAt;

  private int totalQuizCount;

  private int correctQuizCount;

  public void validateAttemptedByUser(Long userId) {
    if (this.user == null) {
      throw new ApiException(ErrorCode.USER_NOT_FOUND, "컬렉션 시도한 유저 정보가 없습니다.");
    }
    if (!this.user.getId().equals(userId)) {
      throw new ApiException(ErrorCode.USER_MISMATCH, "요청한 사용자와 컬렉션 시도한 사용자가 일치하지 않습니다.");
    }
  }

  public void finishSolve(int correct) {
    this.completedAt = LocalDateTime.now();
    this.correctQuizCount = correct;
  }
}
