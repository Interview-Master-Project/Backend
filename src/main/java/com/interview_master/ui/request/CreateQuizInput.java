package com.interview_master.ui.request;

import com.interview_master.domain.Access;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuizInput {

    private Long collectionId;

    private String question;

    private String answer;

    private Access access;
}
