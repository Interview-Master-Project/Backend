package com.interview_master.domain.usercollectionattempt;

import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}
