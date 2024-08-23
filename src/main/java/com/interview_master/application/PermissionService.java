package com.interview_master.application;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.quiz.Quiz;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
    public void checkEditPermission(Long userId, Quiz quiz) {
        if (!userId.equals(quiz.getCreatorId())) {
            throw new ApiException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }

    public void checkEditPermission(Long userId, Collection collection) {
        if (!userId.equals(collection.getCreatorId())) {
            throw new ApiException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }
}
