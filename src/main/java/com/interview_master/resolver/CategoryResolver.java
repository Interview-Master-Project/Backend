package com.interview_master.resolver;

import com.interview_master.domain.category.Category;
import com.interview_master.infrastructure.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryResolver {

  private final CategoryRepository categoryRepository;

  @QueryMapping
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }
}
