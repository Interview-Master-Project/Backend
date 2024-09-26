package com.interview_master.ui;

import com.interview_master.application.ReadCollectionService;
import com.interview_master.domain.collection.Collection;
import com.interview_master.dto.CollectionPage;
import com.interview_master.dto.CollectionWithQuizzes;
import com.interview_master.dto.DataPage;
import com.interview_master.dto.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import static com.interview_master.util.GraphQLAuthUtils.validateUserAuthContext;

@Controller
@RequiredArgsConstructor
public class ReadCollectionResolver {

    private final ReadCollectionService readCollectionService;

    @QueryMapping
    public CollectionWithQuizzes getCollectionWithQuizzes(
            @Argument Long collectionId,
            @ContextValue(required = false) Long userId,
            @ContextValue(name = "authError", required = false) String authError) {

        validateUserAuthContext(userId, authError);
        return readCollectionService.getCollectionWithQuizzes(collectionId, userId);
    }

    /**
     * user의 컬렉션 목록(with paging)
     */
    @QueryMapping
    public CollectionPage userCollection(@Argument DataPage paging, @ContextValue(required = false) Long userId,
        @ContextValue(name = "authError", required = false) String authError) {
        validateUserAuthContext(userId, authError);

        Page<Collection> collections = readCollectionService.userCollections(userId,
            paging.getStart(), paging.getFirst());

        return createCollectionPage(collections);
    }

    /**
     * user가 시도한 컬렉션 목록(with paging)
     */
    @QueryMapping
    public CollectionPage userCollectionHistory(@Argument DataPage paging, @ContextValue(required = false) Long userId,
        @ContextValue(name = "authError", required = false) String authError) {
        validateUserAuthContext(userId, authError);

        Page<Collection> collections = readCollectionService.userAttemptedCollections(userId,
            paging.getStart(), paging.getFirst());

        return createCollectionPage(collections);
    }

    private static CollectionPage createCollectionPage(Page<Collection> collections) {
        return CollectionPage.builder()
            .collections(collections.getContent())
            .pageInfo(PageInfo.builder()
                .hasNextPage(collections.hasNext())
                .currentPage(collections.getNumber() + 1)
                .totalPages(collections.getTotalPages())
                .build())
            .totalCount(collections.getTotalElements())
            .build();
    }
}
