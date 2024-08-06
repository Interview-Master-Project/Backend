package com.interview_master.domain.user;

import com.interview_master.domain.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "user")
@Getter
public class User extends BaseEntity {
    @EmbeddedId
    private UserId id;

    @Embedded
    private Nickname nickname;

    @Embedded
    private Email email;
}

