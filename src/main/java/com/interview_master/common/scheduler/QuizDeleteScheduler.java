package com.interview_master.common.scheduler;

import com.interview_master.infrastructure.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuizDeleteScheduler {

    private final QuizRepository quizRepository;

    @Scheduled(cron = "0 0 2 * * *", zone = "Asia/Seoul")
    public void deleteQuiz() {
        log.info("========== Delete Quiz Start");
        quizRepository.deleteAllByIsDeletedTrueDirect();
        log.info("========== Delete Quiz End");
    }
}