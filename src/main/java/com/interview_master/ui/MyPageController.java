package com.interview_master.ui;

import com.interview_master.application.MyPageService;
import com.interview_master.application.UserProfileService;
import com.interview_master.application.UserQuizAttemptService;
import com.interview_master.dto.CollectionPage;
import com.interview_master.dto.MyPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyPageController {
    private final UserProfileService userProfileService;
    private final MyPageService collectionService;
    private final UserQuizAttemptService userQuizAttemptService;

    @QueryMapping
    public MyPage myPage(@Argument Integer start, @Argument Integer first,
                         @Argument String startDate, @Argument String endDate,
                         @ContextValue(required = false) Long userId,
                         @ContextValue(name = "authError", required = false) String authError) {

        validateUserAuthContext(userId, authError);

        log.info("========== my page\t userId: {}, start: {}, first: {}, startDate : {}, endDate : {}",
                userId, start, first, startDate, endDate);

        return MyPage.builder()
                .user(userProfileService.getProfile(userId))
                .collectionPage(collectionService.userCollections(userId, start, first))
                .quizGardens(userQuizAttemptService.getQuizGardens(LocalDate.parse(startDate), LocalDate.parse(endDate), userId))
                .build();
    }

    @QueryMapping
    public CollectionPage userAttemptedCollections(
            @Argument Integer start, @Argument Integer first,
            @ContextValue(required = false) Long userId, @ContextValue(name = "authError", required = false) String authError) {

        validateUserAuthContext(userId, authError);

        log.info("========== user attempted collections\t userId: {}, start: {}, first: {}", userId, start, first);

        return collectionService.userAttemptedCollections(userId, start, first);
    }
}