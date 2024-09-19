package com.interview_master.ui;

import com.interview_master.application.ReadCollectionService;
import com.interview_master.dto.CollectionWithQuizzes;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ReadCollectionController {

    private final ReadCollectionService readCollectionService;

    @QueryMapping
    public CollectionWithQuizzes getCollectionWithQuizzes(@Argument Long collectionId) {
        return readCollectionService.getCollectionWithQuizzes(collectionId);
    }
}
