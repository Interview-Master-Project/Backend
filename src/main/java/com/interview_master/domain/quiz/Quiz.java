package com.interview_master.domain.quiz;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.Access;
import com.interview_master.domain.BaseEntity;
import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quizzes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public Quiz(String question, String answer, Collection collection, User creator, Access access) {
        setQuestion(question);
        setAnswer(answer);
        setCollection(collection);
        setCreator(creator);
        setAccess(access);
    }

    //==== domain logic ====//
    public void edit(String question, String answer, Collection collection) {
        // 수정할 때는 null이면 그냥 pass하면 된다 -> 예외 던지지 말고
        if (question != null) setQuestion(question);
        if (answer != null) setAnswer(answer);
        if (collection != null) setCollection(collection);
    }

    public void isOwner(Long userId) {
        if (!this.creator.getId().equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN_MODIFICATION);
        }
    }
    //==== setter ====//
    private void setCollection(Collection collection) {
        if (collection == null) throw new ApiException(ErrorCode.NULL_EXCEPTION, "no collectionId");
        if (this.collection == null) this.collection = new Collection();
        if (!collection.getId().equals(this.collection.getId())) {
            this.collection = collection;
        }
    }

    private void setQuestion(String question) {
        if (question == null || question.trim().isEmpty()) throw new ApiException(ErrorCode.NULL_EXCEPTION, "질문 입력은 필수 입니다.");
        this.question = question;
    }

    private void setAnswer(String answer) {
        if (answer == null || answer.trim().isEmpty()) throw new ApiException(ErrorCode.NULL_EXCEPTION, "답변 입력은 필수 입니다.");
        this.answer = answer;
    }

    private void setCreator(User creator) {
        if (creator == null) throw new ApiException(ErrorCode.NULL_EXCEPTION, "작성자 입력은 필수 입니다.");
        this.creator = creator;
    }

    private void setAccess(Access access) {
        if (access == null) throw new ApiException(ErrorCode.NULL_EXCEPTION, "공개 범위 설정은 필수 입니다.");
        this.access = access;
    }
}

