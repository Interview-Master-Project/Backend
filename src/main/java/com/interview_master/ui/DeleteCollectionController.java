package com.interview_master.ui;

import com.interview_master.application.UpsertCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DeleteCollectionController {

    private final UpsertCollectionService upsertCollectionService;

    @QueryMapping
    public String deleteCollection(@Argument Long collectionId) {
        upsertCollectionService.deleteCollection(collectionId);

        return "success";
    }
}
