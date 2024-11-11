package com.interview_master.infrastructure;

import com.interview_master.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {

  User save(User user);


  Optional<User> findByEmailAndIsDeletedFalse(String email);

  Optional<User> findByIdAndIsDeletedFalse(Long id);

  @Query("select u.id from User u where u.isDeleted = true")
  List<Long> findIdsByIsDeletedTrue();

  int deleteByIdIn(List<Long> userIds);

  /**
   * 테스트 용
   */
  Optional<User> findById(Long userId);
}
