package com.interview_master.common.interceptor;

import static com.interview_master.common.constant.TokenConst.TOKEN_HEADER;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.common.token.AuthTokenGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        String accessToken = request.getHeader(TOKEN_HEADER);

        if (accessToken == null) {
            throw new ApiException(ErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
        }

        Long userId = authTokenGenerator.extractUserId(accessToken);

        if (userId != null) {
            RequestAttributes requestContext = Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes());
            requestContext.setAttribute("userId", userId,
                RequestAttributes.SCOPE_REQUEST);
            return true;
        }

        // userId 없는 경우
        throw new ApiException(ErrorCode.BAD_REQUEST, "인증 실패");
    }
}

