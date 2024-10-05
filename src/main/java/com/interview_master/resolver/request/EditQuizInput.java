package com.interview_master.resolver.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditQuizInput {

  private String question;

  private String answer;

  private Long collectionId;
}
