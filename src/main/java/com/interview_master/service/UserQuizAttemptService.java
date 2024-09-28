package com.interview_master.service;

import com.interview_master.dto.QuizGarden;
import com.interview_master.dto.QuizLog;
import com.interview_master.infrastructure.UserQuizAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserQuizAttemptService {

  private final UserQuizAttemptRepository quizAttemptRepository;

  public List<QuizGarden> getQuizGardens(LocalDate startDate, LocalDate endDate, Long userId) {
    List<QuizLog> quizLogs = quizAttemptRepository.findDailyQuizCounts(startDate, endDate, userId);

    Map<LocalDate, Integer> weekIndices = calWeekIndices(startDate, endDate);

    return quizLogs.stream()
        .map(log -> QuizGarden.builder()
            .date(log.getDate())
            .quizzesSolved(log.getQuizzesSolved())
            .dayIndex(calDayIndex(log.getDate()))
            .weekIndex(weekIndices.get(log.getDate()))
            .build())
        .toList(); // 불변 리스트
  }

  /**
   * 날짜의 요일을 인덱스로 반환 0(sun) ~ 6(sat)
   */
  private int calDayIndex(LocalDate date) {
    return date.getDayOfWeek().getValue() % 7;
  }

  /**
   * 주어진 시작일부터 종료일까지 각 날짜의 상대적 주간 인덱스를 계산하여 맵으로 반환 (토요일 지날 때마다 인덱스 증가).
   */
  private Map<LocalDate, Integer> calWeekIndices(LocalDate startDate, LocalDate endDate) {
    Map<LocalDate, Integer> weekIndices = new HashMap<>();
    LocalDate currentDate = startDate;
    int weekIndex = 0;

    while (!currentDate.isAfter(endDate)) {
      weekIndices.put(currentDate, weekIndex);
      if (calDayIndex(currentDate) == 6 && !currentDate.equals(endDate)) {
        weekIndex++;
      }
      currentDate = currentDate.plusDays(1);
    }

    return weekIndices;
  }
}

