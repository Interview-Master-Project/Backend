package com.interview_master.ui;

import com.interview_master.common.token.AuthTokenGenerator;
import com.interview_master.common.token.AuthTokens;
import com.interview_master.domain.user.User;
import com.interview_master.login.OAuthLoginService;
import com.interview_master.login.kakao.KakaoLoginParams;
import com.interview_master.login.naver.NaverLoginParams;
import com.interview_master.ui.response.LoginRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.interview_master.common.constant.Constant.ACCESS_TOKEN_KEY;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthRestController {

  private final OAuthLoginService oAuthLoginService;
  private final AuthTokenGenerator authTokenGenerator;

  @PostMapping("/kakao")
  public ResponseEntity<LoginRes> loginKakao(@RequestBody KakaoLoginParams params) {
    log.info("{} login params {}", "Kakao", params.getAuthorizationCode());
    User user = oAuthLoginService.login(params);
    AuthTokens tokens = authTokenGenerator.generate(user.getId());

    HttpHeaders headers = new HttpHeaders();
    headers.add(ACCESS_TOKEN_KEY, tokens.getAccessToken());

    return ResponseEntity.ok()
        .headers(headers)
        .body(LoginRes.builder()
            .userId(user.getId())
            .nickname(user.getNickname())
            .build());
  }

  @PostMapping("/naver")
  public ResponseEntity<LoginRes> loginNaver(@RequestBody NaverLoginParams params) {
    log.info("{} login params {}, {}", "Naver", params.getAuthorizationCode(),
        params.getState());
    User user = oAuthLoginService.login(params);
    AuthTokens tokens = authTokenGenerator.generate(user.getId());

    HttpHeaders headers = new HttpHeaders();
    headers.add(ACCESS_TOKEN_KEY, tokens.getAccessToken());

    return ResponseEntity.ok()
        .headers(headers)
        .body(LoginRes.builder()
            .userId(user.getId())
            .nickname(user.getNickname())
            .build());
  }
}