package com.interview_master.domain.quiz;


import com.interview_master.domain.user.Nickname;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class QuizCreator {
    @Column(name = "creator_id")
    private Long userId;

    @AttributeOverrides(
        @AttributeOverride(name = "nickname", column = @Column(name = "creator_name"))
    )
    private Nickname name;
}
