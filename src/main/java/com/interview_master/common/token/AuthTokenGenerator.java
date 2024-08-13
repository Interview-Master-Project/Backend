package com.interview_master.common.token;


import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthTokenGenerator {

    @Value("${jwt.bearer-type}")
    private String BEARER_TYPE;

    @Value("${jwt.access-expire-time}")
    private Long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${jwt.refresh-expire-time}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;

    private final JwtTokenProvider jwtTokenProvider;

    public AuthTokens generate(Long userId) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String subject = userId.toString();
        String accessToken = jwtTokenProvider.generate(subject, accessTokenExpiredAt);
        String refreshToken = jwtTokenProvider.generate(subject, refreshTokenExpiredAt);

        return AuthTokens.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }

    public Long extractUserId(String accessToken) {
        return Long.valueOf(jwtTokenProvider.extractSubject(accessToken));
    }

}
