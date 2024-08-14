package com.interview_master.ui.request;

import com.interview_master.domain.Access;
import lombok.Getter;

@Getter
public class EditQuizDTO {
    private final String newQuestion;
    private final String newAnswer;
    private final Access newAccess;

    public EditQuizDTO(String newQuestion, String newAnswer, Access newAccess) {
        this.newQuestion = newQuestion;
        this.newAnswer = newAnswer;
        this.newAccess = newAccess;
    }

}
