package com.interview_master.domain.quiz;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.Access;
import com.interview_master.domain.BaseEntity;
import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "quizzes")
@jakarta.persistence.Access(AccessType.FIELD)
@Getter
public class Quiz extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Enumerated(EnumType.STRING)
    private Access access;

    protected Quiz() {
    }

    public Quiz(Collection collection, String question, String answer, User creator, Access access) {
        setCollection(collection);
        setQuestion(question);
        setAnswer(answer);
        setCreator(creator);
        setAccess(access);
    }

    //==== domain logic ====//
//    public void edit(EditQuizInput editQuizInput) {
//        // 수정할 때는 null이면 그냥 pass하면 된다 -> 예외 던지지 말고
//        if (editQuizInput.question() != null) setQuestion(editQuizInput.question());
//        if (editQuizInput.answer() != null) setAnswer(editQuizInput.answer());
//        if (editQuizInput.collectionId() != null) setCollectionId(editQuizInput.collectionId());
//        if (editQuizInput.access() != null ) setAccess(editQuizInput.access());
//    }

    public void checkExistence() {
        if(this.getIsDeleted()) throw new ApiException(ErrorCode.QUIZ_NOT_FOUND);
    }

    //==== setter ====//
    private void setCollection(Collection collection) {
        if (collection == null) throw new ApiException(ErrorCode.NULL_EXCEPTION, "no collectionId");
        if (!collection.getId().equals(this.collection.getId())) {
            this.collection = collection;
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

    private void setCreator(User creator) {
        if (creator == null) throw new ApiException(ErrorCode.NULL_EXCEPTION, "no creatorId");
        this.creator = creator;
    }

    private void setAccess(Access access) {
        if (access == null) throw new ApiException(ErrorCode.NULL_EXCEPTION, "no access");
        this.access = access;
    }
}

