package com.interview_master.domain.user;

import com.interview_master.domain.BaseEntity;
import com.interview_master.login.OAuthProvider;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    protected User() {}

    public User(Nickname nickname, Email email, OAuthProvider oAuthProvider) {
        setNickname(nickname);
        setEmail(email);
        setOAuthProvider(oAuthProvider);
    }

    private void setNickname(Nickname nickname) {
        this.nickname = nickname;
    }

    private void setEmail(Email email) {
        this.email = email;
    }

    private void setOAuthProvider(OAuthProvider oAuthProvider) {
        this.oAuthProvider = oAuthProvider;
    }
}

