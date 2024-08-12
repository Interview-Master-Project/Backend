package com.interview_master.ui;

import com.interview_master.domain.user.User;
import com.interview_master.login.OAuthLoginParams;
import com.interview_master.login.kakao.KakaoLoginParams;
import com.interview_master.login.naver.NaverLoginParams;
import com.interview_master.login.OAuthLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.interview_master.common.constant.SessionConst.LOGIN_USER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final OAuthLoginService oAuthLoginService;

    @PostMapping("/kakao")
    public ResponseEntity<Object> loginKakao(@RequestBody KakaoLoginParams params, HttpServletRequest request) {
        log.info("{} login params {}", "Kakao", params.getAuthorizationCode());
        User user = login(params, request);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/naver")
    public ResponseEntity<User> loginNaver(@RequestBody NaverLoginParams params, HttpServletRequest request) {
        log.info("{} login params {}, {}", "Naver", params.getAuthorizationCode(), params.getState());
        User user = login(params, request);
        log.info("{} login user info {}, {}", "Naver", user.getId(), user.getNickname());

        return ResponseEntity.ok(user);
    }

    private User login(OAuthLoginParams params, HttpServletRequest request) {
        User user = oAuthLoginService.login(params);

        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_USER, user);
        return user;
    }
}