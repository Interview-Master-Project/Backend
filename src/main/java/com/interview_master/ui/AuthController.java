package com.interview_master.ui;

import static com.interview_master.common.constant.SessionConst.LOGIN_USER;

import com.interview_master.domain.user.User;
import com.interview_master.login.NaverLoginParams;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final OAuthLoginService oAuthLoginService;

//    @PostMapping("/kakao")
//    public ResponseEntity<Object> loginKakao(@RequestBody KakaoLoginParams params) {
//        return ResponseEntity.ok(oAuthLoginService.login(params));
//    }

    @PostMapping("/naver")
    public ResponseEntity<User> loginNaver(@RequestBody NaverLoginParams params, HttpServletRequest request) {
        log.info("login params {}, {}", params.getAuthorizationCode(), params.getState());
        User user = oAuthLoginService.login(params);

        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_USER, user);
        
        return ResponseEntity.ok(user);
    }
}