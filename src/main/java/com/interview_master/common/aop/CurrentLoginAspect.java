//package com.interview_master.common.aop;
//
//import com.interview_master.common.exception.ApiException;
//import com.interview_master.common.exception.ErrorCode;
//import com.interview_master.domain.user.User;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.util.Objects;
//
//import static com.interview_master.common.constant.SessionConst.LOGIN_USER;
//
//@Aspect
//@Component
//@Slf4j
//public class CurrentLoginAspect {
//
//    @Pointcut("execution(* *(.., @com.interview_master.common.annotation.CurrentUser (*), ..))")
//    public void loginRequired() {
//    }
//
//    @Around("loginRequired()")
//    public Object checkSession(ProceedingJoinPoint joinPoint) throws Throwable {
//
//        log.info("AOP - @CurrentUser Check Started");
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
//        HttpSession session = request.getSession(false);
//
//        if (session == null) {
//            log.info("AOP - @CurrentUser Check Result - session empty");
//            throw new ApiException(ErrorCode.UNAUTHORIZED_USER);
//        }
//
//        User user = (User) session.getAttribute(LOGIN_USER);
//        if (user == null) {
//            log.info("AOP - @CurrentUser Check Result - user empty");
//            throw new ApiException(ErrorCode.UNAUTHORIZED_USER);
//        }
//        Object[] args = joinPoint.getArgs();
//        for (int i = 0; i < args.length; i++) {
//            if (args[i] instanceof User) {
//                args[i] = user;
//            }
//        }
//
//        return joinPoint.proceed(args);
//    }
//
//}
