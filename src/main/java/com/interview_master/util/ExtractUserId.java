package com.interview_master.util;

import static com.interview_master.common.constant.Constant.USER_ID;

import java.util.Objects;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class ExtractUserId {

    public static Long extractUserIdFromContextHolder() {
        return (Long) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())
            .getAttribute(USER_ID, RequestAttributes.SCOPE_REQUEST);
    }

}
