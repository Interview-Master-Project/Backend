package com.interview_master.ui.response;

import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.domain.quizresult.QuizResult;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class QuizWithCollectionAndResults {

    private Quiz quiz;
    private Collection collection;
    private QuizResult quizResult;
}
