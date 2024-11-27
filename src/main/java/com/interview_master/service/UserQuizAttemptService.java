package com.interview_master.service;

import com.interview_master.domain.quiz.Quiz;
import com.interview_master.domain.user.User;
import com.interview_master.domain.usercollectionattempt.UserCollectionAttempt;
import com.interview_master.domain.userquizattempt.UserQuizAttempt;
import com.interview_master.dto.QuizGarden;
import com.interview_master.dto.QuizLog;
import com.interview_master.infrastructure.UserQuizAttemptRepository;
import com.interview_master.resolver.request.QuizResultInput;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserQuizAttemptService {

  private final UserQuizAttemptRepository quizAttemptRepository;
  private final EntityManager em;

  public List<QuizGarden> getQuizGardens(LocalDate startDate, LocalDate endDate, Long userId) {
    List<QuizLog> quizLogs = quizAttemptRepository.findDailyQuizCounts(startDate, endDate, userId);

    return quizLogs.stream()
        .map(log -> QuizGarden.builder()
            .date(log.getDate())
            .quizzesSolved(log.getQuizzesSolved())
            .build())
        .toList(); // 불변 리스트
  }

  @Transactional
  public void submitQuizResults(Long userCollectionAttemptId, Long userId,
      List<QuizResultInput> quizResults) {
    List<UserQuizAttempt> uqaList = quizResults.stream()
        .map(qr -> UserQuizAttempt.builder()
            .quiz(em.getReference(Quiz.class, qr.getQuizId()))
            .user(em.getReference(User.class, userId))
            .collectionAttempt(
                em.getReference(UserCollectionAttempt.class, userCollectionAttemptId))
            .answeredAt(qr.getAnsweredAt())
            .isCorrect(qr.getCorrect())
            .build())
        .toList();
    // TODO : 현재는 uqaList 개수만큼 insert문 발생 -> bulk insert문으로 수정해 1개의 insert문만 발생하도록 리팩토링하기
    quizAttemptRepository.saveAll(uqaList);
  }

  public List<UserQuizAttempt> getUserQuizAttempts(Long quizId, Long userId) {
    return quizAttemptRepository.findAllByQuizIdAndUserIdOrderByAnsweredAtDesc(quizId, userId);
  }

  /**
   * collectionAttempt에 속한 퀴즈 시도 기록들 가져오기
   */
  public List<UserQuizAttempt> getQuizzesAttemptsByCollectionAttempt(Long ucaId, Long userId) {
    return quizAttemptRepository.findAllByCollectionAttemptIdAndUserIdOrderByQuizId(ucaId, userId);
  }

}

