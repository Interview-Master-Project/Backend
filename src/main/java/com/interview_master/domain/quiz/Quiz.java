package com.interview_master.domain.quiz;

import com.interview_master.domain.Access;
import com.interview_master.domain.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "quiz")
@jakarta.persistence.Access(AccessType.FIELD)
public class Quiz extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private CollectionInfo collectionInfo;

    @Embedded
    private Question question;

    @Embedded
    private Answer answer;

    private Long creatorId;

    @Enumerated(EnumType.STRING)
    private Access access;

    protected Quiz() {
    }

    public Quiz(CollectionInfo collectionInfo, Question question, Answer answer, Long creatorId, Access access) {
        setCollectionInfo(collectionInfo);
        setQuestion(question);
        setAnswer(answer);
        setCreatorId(creatorId);
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

    private void setCreatorId(Long creatorId) {
        if (creatorId == null) throw new IllegalArgumentException("no creator");
        this.creatorId = creatorId;
    }
}

