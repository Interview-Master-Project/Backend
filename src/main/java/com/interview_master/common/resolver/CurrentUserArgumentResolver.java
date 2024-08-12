package com.interview_master.common.resolver;

import com.interview_master.common.annotation.CurrentUser;
import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.user.User;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.graphql.data.method.HandlerMethodArgumentResolver;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.interview_master.common.constant.SessionConst.LOGIN_USER;

@Slf4j
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(CurrentUser.class);
        boolean hasUserType = User.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginAnnotation && hasUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, DataFetchingEnvironment environment) throws Exception {
        log.info("resolveArgument 실행");
        // 현재 요청의 HttpServletRequest를 가져옴
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        HttpSession session = request.getSession(false);
        if (session == null) {
            log.info("Resolver - @CurrentUser Check Result - session empty");
            return new ApiException(ErrorCode.UNAUTHORIZED_USER);
        }

        User user = (User) session.getAttribute(LOGIN_USER);
        if (user == null) {
            log.info("Resolver - @CurrentUser Check Result - user not found");
            return new ApiException(ErrorCode.UNAUTHORIZED_USER);
        }

        return user;
    }

}
