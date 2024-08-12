package com.interview_master.ui;

import com.interview_master.application.CollectionService;
import com.interview_master.domain.collection.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @QueryMapping
    public Collection collectionById(@Argument Long id) {
        return collectionService.findById(id);
    }

}
