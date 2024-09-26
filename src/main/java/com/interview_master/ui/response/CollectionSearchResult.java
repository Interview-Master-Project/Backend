package com.interview_master.ui.response;


import com.interview_master.domain.category.Category;
import com.interview_master.domain.collection.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionSearchResult {

    private Collection collection;

    private Category category;

    private int quizCount;

    private int correctRate;
}
