package com.interview_master.dto;

import com.interview_master.domain.collection.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionWithQuizzes {

    private Collection collection;
    private List<QuizWithAttempts> quizzesWithAttempts;
}
