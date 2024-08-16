package com.interview_master.domain.quiz;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.Access;
import com.interview_master.domain.BaseEntity;
import com.interview_master.ui.request.EditQuizInput;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "quiz")
@jakarta.persistence.Access(AccessType.FIELD)
@Getter
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
        setCollectionId(collectionId);
        setQuestion(question);
        setAnswer(answer);
        setCreatorId(creatorId);
        setAccess(access);
    }

    // domain logic
    public void edit(EditQuizInput editQuizDTO) {
        // 수정할 때는 null이면 그냥 pass하면 된다 -> 예외 던지지 말고
        setQuestion(editQuizDTO.question());
        setAnswer(editQuizDTO.answer());
        setCollectionId(editQuizDTO.collectionId());
        setAccess(editQuizDTO.access());
    }

    // setter
    private void setCollectionId(Long collectionId) {
        if (collectionId == null) throw new ApiException(ErrorCode.NULL_EXCEPTION, "no collectionId");
        if (!collectionId.equals(this.collectionId)) {
            this.collectionId = collectionId;
        }
    }

    private void setQuestion(String question) {
        if (question == null) throw new ApiException(ErrorCode.NULL_EXCEPTION, "no question");
        if (!question.trim().isEmpty()) {
            this.question = question;
        }
    }

    private void setAnswer(String answer) {
        if (answer == null) throw new ApiException(ErrorCode.NULL_EXCEPTION, "no answer");
        if (!answer.trim().isEmpty()) {
            this.answer = answer;
        }
    }

    private void setCreatorId(Long creatorId) {
        if (creatorId == null) throw new ApiException(ErrorCode.NULL_EXCEPTION, "no creatorId");
        this.creatorId = creatorId;
    }

    private void setAccess(Access access) {
        if (access == null) throw new ApiException(ErrorCode.NULL_EXCEPTION, "no access");
        this.access = access;
    }
}

