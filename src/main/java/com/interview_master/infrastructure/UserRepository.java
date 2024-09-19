package com.interview_master.infrastructure;

import com.interview_master.domain.user.User;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndIsDeletedFalse(Long id);
}
