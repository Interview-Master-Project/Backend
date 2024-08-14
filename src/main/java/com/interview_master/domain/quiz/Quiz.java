package com.interview_master.domain.quiz;

import com.interview_master.domain.Access;
import com.interview_master.domain.BaseEntity;
import com.interview_master.ui.request.EditQuizDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "quiz")
@jakarta.persistence.Access(AccessType.FIELD)
public class Quiz extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "collection_id")
    private Long collectionId;

    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    private Long creatorId;

    @Enumerated(EnumType.STRING)
    private Access access;

    protected Quiz() {
    }

    public Quiz(Long collectionId, String question, String answer, Long creatorId, Access access) {
        setCollectionInfo(collectionId);
        setQuestion(question);
        setAnswer(answer);
        setCreatorId(creatorId);
        this.access = access;
    }

    // domain logic
    public void edit(EditQuizDTO editQuizDTO) {
        String newQuestion = editQuizDTO.getNewQuestion();
        String newAnswer = editQuizDTO.getNewAnswer();
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
    private void setCollectionInfo(Long collectionId) {
        if (collectionId == null) throw new IllegalArgumentException("no collection info");
        this.collectionId = collectionId;
    }

    private void setQuestion(String question) {
        if (question == null) throw new IllegalArgumentException("no question");
        this.question = question;
    }

    private void setAnswer(String answer) {
        if (answer == null) throw new IllegalArgumentException("no answer");
        this.answer = answer;
    }

    private void setCreatorId(Long creatorId) {
        if (creatorId == null) throw new IllegalArgumentException("no creator");
        this.creatorId = creatorId;
    }
}

