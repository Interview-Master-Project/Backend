package com.interview_master.domain.userquizattempt;

import com.interview_master.domain.quiz.Quiz;
import com.interview_master.domain.user.User;
import com.interview_master.domain.usercollectionattempt.UserCollectionAttempt;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_quiz_attempts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQuizAttempt {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "collection_attempt_id")
  private UserCollectionAttempt collectionAttempt;

  @ManyToOne(fetch = FetchType.LAZY)
  private Quiz quiz;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  private Boolean isCorrect;

  private LocalDateTime answeredAt;
}
