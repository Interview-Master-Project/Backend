package com.interview_master.ui;

import com.interview_master.application.CreateQuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class CreateQuizController {
    private final CreateQuizService createQuizService;

    @PostMapping()
    public ResponseEntity<String> createQuiz(@RequestBody @Valid QuizRequest quizRequest ) {

        return ResponseEntity.ok("질문 생성 성공!");
    }


}
