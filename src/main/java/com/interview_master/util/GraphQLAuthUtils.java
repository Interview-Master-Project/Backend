package com.interview_master.util;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;

public class GraphQLAuthUtils {
    public static void validateUserAuthContext(Long userId, String authError) {
        if (authError != null) {
            if ("NO_TOKEN".equals(authError)) {
                throw new ApiException(ErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
            } else if ("INVALID_TOKEN".equals(authError)) {
                throw new ApiException(ErrorCode.INVALID_TOKEN);
            }
        }

        if (userId == null) {
            throw new ApiException(ErrorCode.SERVER_ERROR, "userId is null");
        }
    }
}
