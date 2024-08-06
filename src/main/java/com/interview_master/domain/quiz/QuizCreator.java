package com.interview_master.domain.quiz;


import com.interview_master.domain.user.UserId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class QuizCreator {
    @AttributeOverrides(
        @AttributeOverride(name = "id", column = @Column(name = "creator_id"))
    )
    private UserId userId;

    @Column(name = "creator_name")
    private String name;
}
