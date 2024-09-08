package com.interview_master.ui;

import com.interview_master.application.CollectionService;
import com.interview_master.application.UserProfileService;
import com.interview_master.domain.user.User;
import com.interview_master.dto.CollectionPage;
import com.interview_master.dto.MyPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyPageController {
    private final UserProfileService userProfileService;
    private final CollectionService collectionService;

    @QueryMapping
    public MyPage myPage(@Argument Long userId, @Argument Integer offset, @Argument Integer limit) {
        log.info("========== my page\t userId: {}, offset: {}, limit: {}", userId, offset, limit);

        User profile = userProfileService.getProfile(userId);
        CollectionPage collectionPage = collectionService.userCollections(userId, offset, limit);

        return MyPage.builder()
                .user(profile)
                .collectionPage(collectionPage)
                .build();
    }

    @QueryMapping
    public CollectionPage userAttemptedCollections(@Argument Long userId, @Argument Integer offset, @Argument Integer limit) {
        log.info("========== user attempted collections\t userId: {}, offset: {}, limit: {}", userId, offset, limit);

        return collectionService.userAttemptedCollections(userId, offset, limit);
    }
}
