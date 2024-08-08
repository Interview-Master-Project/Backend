package com.interview_master.ui;

import com.interview_master.application.CreateQuizService;
import com.interview_master.domain.user.Email;
import com.interview_master.domain.user.Nickname;
import com.interview_master.domain.user.User;
import com.interview_master.login.OAuthProvider;
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
        User user = new User(new Nickname("nick"), new Email("andantej99@naver.com"), OAuthProvider.NAVER);
        createQuizService.createQuiz(quizRequest, user);
        return ResponseEntity.ok("질문 생성 성공!");
    }


}