package com.interview_master.ui.response;

import com.interview_master.domain.quiz.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class QuizWithCollectionNameAndResults {

    private Quiz quiz;
    private String collectionName;
    private Integer correctAttempts;
    private Integer wrongAttempts;
}
