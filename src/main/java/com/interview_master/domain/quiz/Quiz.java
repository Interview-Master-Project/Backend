package com.interview_master.domain.quiz;

import com.interview_master.domain.Access;
import com.interview_master.domain.BaseEntity;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "quiz")
@jakarta.persistence.Access(AccessType.FIELD)
public class Quiz extends BaseEntity {
    @EmbeddedId
    private QuizId id;

    @Embedded
    private CollectionInfo collectionInfo;

    @Embedded
    private Question question;

    @Embedded
    private Answer answer;

    @Embedded
    private QuizCreator creator;

    @Enumerated(EnumType.STRING)
    private Access access;

    protected Quiz() {
    }

    public Quiz(CollectionInfo collectionInfo, Question question, Answer answer, QuizCreator creator, Access access) {
        setCollectionInfo(collectionInfo);
        setQuestion(question);
        setAnswer(answer);
        setCreator(creator);
        this.access = access;
    }

    // domain logic
    public void edit(EditQuizDTO editQuizDTO) {
        Question newQuestion = editQuizDTO.getNewQuestion();
        Answer newAnswer = editQuizDTO.getNewAnswer();
        Access newAccess = editQuizDTO.getNewAccess();

        boolean questionChanged = newQuestion != null && !newQuestion.equals(this.question);
        boolean answerChanged = newAnswer != null && !newAnswer.equals(this.answer);
        boolean accessChanged = newAccess != null && newAccess != this.access;

        if (questionChanged) {
            setQuestion(newQuestion);
        }
        if (answerChanged) {
            setAnswer(newAnswer);
        }
        if (accessChanged) {
            this.access = newAccess;
        }
    }

    // setter
    private void setCollectionInfo(CollectionInfo collectionInfo) {
        if (collectionInfo == null) throw new IllegalArgumentException("no collection info");
        this.collectionInfo = collectionInfo;
    }

    private void setQuestion(Question question) {
        if (question == null) throw new IllegalArgumentException("no question");
        this.question = question;
    }

    private void setAnswer(Answer answer) {
        if (answer == null) throw new IllegalArgumentException("no answer");
        this.answer = answer;
    }

    private void setCreator(QuizCreator creator) {
        if (creator == null) throw new IllegalArgumentException("no creator");
        this.creator = creator;
    }
}

