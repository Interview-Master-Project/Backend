package com.interview_master.domain.quiz;

import com.interview_master.domain.Access;
import com.interview_master.domain.BaseEntity;

public class Quiz extends BaseEntity {
    private QuizId id;
    private CollectionInfo collectionInfo;
    private Question question;
    private Answer answer;
    private QuizCreator creator;
    private Access access;

    public Quiz(QuizId id, CollectionInfo collectionInfo, Question question, Answer answer, QuizCreator creator, Access access) {
        setId(id);
        setCollectionInfo(collectionInfo);
        setQuestion(question);
        setAnswer(answer);
        setCreator(creator);
        this.access = access;
    }

    // domain logic


    // setter
    private void setId(QuizId id) {
        this.id = id;
    }

    private void setCollectionInfo(CollectionInfo collectionInfo) {
        this.collectionInfo = collectionInfo;
    }

    private void setQuestion(Question question) {
        this.question = question;
    }

    private void setAnswer(Answer answer) {
        this.answer = answer;
    }

    private void setCreator(QuizCreator creator) {
        this.creator = creator;
    }
}

