package com.interview_master.domain.quiz;

import com.interview_master.domain.Access;
import lombok.Getter;

@Getter
public class EditQuizDTO {
    private final Question newQuestion;
    private final Answer newAnswer;
    private final Access newAccess;

    public EditQuizDTO(Question newQuestion, Answer newAnswer, Access newAccess) {
        this.newQuestion = newQuestion;
        this.newAnswer = newAnswer;
        this.newAccess = newAccess;
    }

}
