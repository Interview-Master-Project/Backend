package com.interview_master.common.interceptor;

import static com.interview_master.common.constant.SessionConst.LOGIN_USER;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse
        response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(LOGIN_USER) == null) {
            log.info("미인증 사용자 요청");
            throw new ApiException(ErrorCode.UNAUTHORIZED_USER);
        }
        return true;
    }
}

