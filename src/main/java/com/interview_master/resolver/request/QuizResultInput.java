package com.interview_master.resolver.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResultInput {

  private Long quizId;
  private Boolean correct;
  private LocalDateTime answeredAt;
}
