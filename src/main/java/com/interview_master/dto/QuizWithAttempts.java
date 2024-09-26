package com.interview_master.dto;

import com.interview_master.domain.quiz.Quiz;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizWithAttempts {

  private Quiz quiz;
  private long totalAttempts;
  private long correctAttempts;
  private LocalDateTime recentAnswerAt;
}
