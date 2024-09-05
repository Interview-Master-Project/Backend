package com.interview_master.domain.user;

import com.interview_master.domain.BaseEntity;
import com.interview_master.login.OAuthProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String email;

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    private void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private void setOAuthProvider(OAuthProvider oAuthProvider) {
        this.oAuthProvider = oAuthProvider;
    }
}

