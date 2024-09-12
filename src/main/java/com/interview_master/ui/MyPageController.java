package com.interview_master.ui;

import com.interview_master.application.MyPageService;
import com.interview_master.application.UserProfileService;
import com.interview_master.application.UserQuizAttemptService;
import com.interview_master.domain.user.User;
import com.interview_master.dto.CollectionPage;
import com.interview_master.dto.MyPage;
import com.interview_master.dto.QuizGarden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyPageController {
    private final UserProfileService userProfileService;
    private final MyPageService collectionService;
    private final UserQuizAttemptService userQuizAttemptService;

    @QueryMapping
    public MyPage myPage(@Argument Long userId, @Argument Integer offset, @Argument Integer limit,
                         @Argument String startDate, @Argument String endDate) {
        log.info("========== my page\t userId: {}, offset: {}, limit: {}, startDate : {}, endDate : {}",
                userId, offset, limit, startDate, endDate);

        User profile = userProfileService.getProfile(userId);
        CollectionPage collectionPage = collectionService.userCollections(userId, offset, limit);
        List<QuizGarden> quizGardens = userQuizAttemptService.getQuizGardens(LocalDate.parse(startDate), LocalDate.parse(endDate), userId);

        return MyPage.builder()
                .user(profile)
                .collectionPage(collectionPage)
                .quizGardens(quizGardens)
                .build();
    }

    @QueryMapping
    public CollectionPage userAttemptedCollections(@Argument Long userId, @Argument Integer offset, @Argument Integer limit) {
        log.info("========== user attempted collections\t userId: {}, offset: {}, limit: {}", userId, offset, limit);

        return collectionService.userAttemptedCollections(userId, offset, limit);
    }
}