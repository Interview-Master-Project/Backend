package com.interview_master.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizzesWithAttemptPaging {

    private List<QuizWithAttempt> quizzesWithAttempt;

    private PageInfo pageInfo;
}
