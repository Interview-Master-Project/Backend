package com.interview_master.ui;

import com.interview_master.application.ReadCollectionService;
import com.interview_master.dto.CollectionWithQuizzes;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

@Controller
@RequiredArgsConstructor
public class ReadCollectionController {

    private final ReadCollectionService readCollectionService;

    @QueryMapping
    public CollectionWithQuizzes getCollectionWithQuizzes(
            @Argument Long collectionId,
            @ContextValue(required = false) Long userId,
            @ContextValue(name = "authError", required = false) String authError) {

        validateUserAuthContext(userId, authError);
        return readCollectionService.getCollectionWithQuizzes(collectionId, userId);
    }
}
