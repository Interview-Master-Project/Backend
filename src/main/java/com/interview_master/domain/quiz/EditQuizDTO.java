package com.interview_master.domain.quiz;

import com.interview_master.domain.Access;
import lombok.Getter;

@Getter
public class EditQuizDTO {
    private final String newAnswer;
    private final Access newAccess;

    public EditQuizDTO(String newAnswer, Access newAccess) {
        this.newAnswer = newAnswer;
        this.newAccess = newAccess;
    }

}
