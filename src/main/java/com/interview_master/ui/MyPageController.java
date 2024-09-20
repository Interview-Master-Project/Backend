package com.interview_master.ui;

import com.interview_master.application.MyPageService;
import com.interview_master.application.UserProfileService;
import com.interview_master.application.UserQuizAttemptService;
import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.dto.CollectionPage;
import com.interview_master.dto.MyPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyPageController {
    private final UserProfileService userProfileService;
    private final MyPageService collectionService;
    private final UserQuizAttemptService userQuizAttemptService;

    @QueryMapping
    public MyPage myPage(@Argument Integer offset, @Argument Integer limit,
                         @Argument String startDate, @Argument String endDate,
                         @ContextValue(required = false) Long userId,
                         @ContextValue(name = "authError", required = false) String authError) {

        validateUserAuthContext(userId, authError);

        log.info("========== my page\t userId: {}, offset: {}, limit: {}, startDate : {}, endDate : {}",
                userId, offset, limit, startDate, endDate);

        return MyPage.builder()
                .user(userProfileService.getProfile(userId))
                .collectionPage(collectionService.userCollections(userId, offset, limit))
                .quizGardens(userQuizAttemptService.getQuizGardens(LocalDate.parse(startDate), LocalDate.parse(endDate), userId))
                .build();
    }

    @QueryMapping
    public CollectionPage userAttemptedCollections(
            @Argument Integer offset, @Argument Integer limit,
            @ContextValue(required = false) Long userId, @ContextValue(name = "authError", required = false) String authError) {

        validateUserAuthContext(userId, authError);

        log.info("========== user attempted collections\t userId: {}, offset: {}, limit: {}", userId, offset, limit);

        return collectionService.userAttemptedCollections(userId, offset, limit);
    }

    private static void validateUserAuthContext(Long userId, String authError) {
        if (authError != null) {
            if ("NO_TOKEN".equals(authError)) {
                throw new ApiException(ErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
            } else if ("INVALID_TOKEN".equals(authError)) {
                throw new ApiException(ErrorCode.INVALID_TOKEN);
            }
        }

        if (userId == null) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND);
        }
    }
}