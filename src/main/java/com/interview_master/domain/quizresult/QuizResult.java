package com.interview_master.domain.quizresult;

import com.interview_master.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "quiz_result")
@Getter
public class QuizResult extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quizId;
    private Long userId;

    private int correctAttempts;
    private int wrongAttempts;

    protected QuizResult() {}

    public QuizResult(Long quizId, Long userId, int correctAttempt, int wrongAttempt) {
        setQuizId(quizId);
        setUserId(userId);
        setCorrectAttempts(correctAttempt);
        setWrongAttempts(wrongAttempt);
    }

    public void correct() {
        this.correctAttempts++;
    }

    public void wrong() {
        this.wrongAttempts++;
    }

    private void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    private void setUserId(Long userId) {
        this.userId = userId;
    }

    private void setCorrectAttempts(int correctAttempts) {
        this.correctAttempts = correctAttempts;
    }

    private void setWrongAttempts(int wrongAttempts) {
        this.wrongAttempts = wrongAttempts;
    }
}
