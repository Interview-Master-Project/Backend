package com.interview_master.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizGarden {

    private LocalDate date;
    private Integer quizzesSolved;
    private Integer dayIndex;
    private Integer weekIndex;
}