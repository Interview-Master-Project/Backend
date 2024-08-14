package com.interview_master.ui;

import com.interview_master.common.token.AuthTokens;
import com.interview_master.login.OAuthLoginService;
import com.interview_master.login.kakao.KakaoLoginParams;
import com.interview_master.login.naver.NaverLoginParams;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
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

    private void setCookie(String accessToken, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("authorization-token", accessToken)
            .httpOnly(true)
            //.secure(true)
            .path("/")
            .maxAge(1800000)
            .sameSite("Strict")
            .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    @PostMapping("/kakao")
    public ResponseEntity<String> loginKakao(@RequestBody KakaoLoginParams params, HttpServletResponse response) {
        log.info("{} login params {}", "Kakao", params.getAuthorizationCode());
        AuthTokens tokens = oAuthLoginService.login(params);
        setCookie(tokens.getAccessToken(), response);
        return ResponseEntity.ok("로그인에 성공했습니다!");
    }

    @PostMapping("/naver")
    public ResponseEntity<String> loginNaver(@RequestBody NaverLoginParams params, HttpServletResponse response) {
        log.info("{} login params {}, {}", "Naver", params.getAuthorizationCode(),
            params.getState());
        AuthTokens tokens = oAuthLoginService.login(params);
        setCookie(tokens.getAccessToken(), response);
        return ResponseEntity.ok("로그인에 성공했습니다!");
    }


}