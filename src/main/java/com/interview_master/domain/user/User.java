package com.interview_master.domain.user;

import com.interview_master.domain.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "member")
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Nickname nickname;

    @Embedded
    private Email email;

    protected User() {}

    public User(Nickname nickname, Email email) {
        setNickname(nickname);
        setEmail(email);
    }

    private void setNickname(Nickname nickname) {
        this.nickname = nickname;
    }

    private void setEmail(Email email) {
        this.email = email;
    }
}

