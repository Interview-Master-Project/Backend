package com.interview_master.ui;

import com.interview_master.common.token.AuthTokens;
import com.interview_master.login.OAuthLoginService;
import com.interview_master.login.kakao.KakaoLoginParams;
import com.interview_master.login.naver.NaverLoginParams;
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

    @PostMapping("/kakao")
    public ResponseEntity<AuthTokens> loginKakao(@RequestBody KakaoLoginParams params) {
        log.info("{} login params {}", "Kakao", params.getAuthorizationCode());
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }

    @PostMapping("/naver")
    public ResponseEntity<AuthTokens> loginNaver(@RequestBody NaverLoginParams params) {
        log.info("{} login params {}, {}", "Naver", params.getAuthorizationCode(),
            params.getState());
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }
}