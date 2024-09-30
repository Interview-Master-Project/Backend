package com.interview_master.infrastructure;

import com.interview_master.domain.category.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends Repository<Category, Long> {

    Optional<Category> findById(Long id);

    List<Category> findAll();

  @Query("select count(c) from Category c where c.id in :ids")
  Integer countByIdIn(List<Long> ids);
}
