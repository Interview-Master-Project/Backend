package com.interview_master.infrastructure;

import com.interview_master.domain.user.User;
import java.util.List;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {

  User save(User user);

  Optional<User> findById(Long userId);

  Optional<User> findByEmailAndIsDeletedFalse(String email);

  Optional<User> findByIdAndIsDeletedFalse(Long id);

  List<User> findByIsDeletedTrue();

  int deleteByIdIn(List<Long> userIds);
}
