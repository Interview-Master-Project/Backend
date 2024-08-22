package com.interview_master.common.interceptor;

import static com.interview_master.common.constant.Constant.TOKEN_KEY;
import static com.interview_master.common.constant.Constant.USER_ID;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.common.token.AuthTokenGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final AuthTokenGenerator authTokenGenerator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse
        response, Object handler) {
        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);

        // CORS 방지 : OPTIONS 메서드로 사전 요청 오면 다 pass
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        /*


        String token = null;

        // 모든 쿠키를 가져와서 "authorization-token" 쿠키를 찾음
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (TOKEN_KEY.equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            throw new ApiException(ErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
        }

        Long userId = authTokenGenerator.extractUserId(token);
        */
        // 테스트 용
        Long userId = 4L;

        if (userId != null) {
            RequestAttributes requestContext = Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes());
            requestContext.setAttribute(USER_ID, userId,
                RequestAttributes.SCOPE_REQUEST);
            return true;
        }

        // userId 없는 경우
        throw new ApiException(ErrorCode.BAD_REQUEST, "인증 실패");
    }
}

