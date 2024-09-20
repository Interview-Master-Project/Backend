package com.interview_master.ui;

import com.interview_master.application.UpsertCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

@Controller
@RequiredArgsConstructor
public class DeleteCollectionController {

    private final UpsertCollectionService upsertCollectionService;

    @QueryMapping
    public String deleteCollection(@Argument Long collectionId,
                                   @ContextValue(required = false) Long userId,
                                   @ContextValue(required = false) String authError) {

        validateUserAuthContext(userId, authError);

        upsertCollectionService.deleteCollection(collectionId, userId);

        return "success";
    }

}
