package com.interview_master.infrastructure;

import com.interview_master.domain.category.Category;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CategoryRepository extends Repository<Category, Long> {

    void save(Category category);

    Optional<Category> findById(Long id);

}
