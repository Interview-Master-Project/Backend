package com.interview_master.ui.response;

import com.interview_master.domain.category.Category;
import com.interview_master.domain.collection.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CollectionWithCategoryAndQuizCount {

    private Long quizCount;
    private Long correctAttempts;
    private Long wrongAttempts;
    private Collection collection;
    private Category category;
}
