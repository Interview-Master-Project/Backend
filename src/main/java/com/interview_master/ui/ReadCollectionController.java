package com.interview_master.ui;

import com.interview_master.application.ReadCollectionService;
import com.interview_master.ui.response.CollectionWithCategoryAndQuizCountAndResults;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReadCollectionController {

    private final ReadCollectionService readCollectionService;


    @QueryMapping
    public List<CollectionWithCategoryAndQuizCountAndResults> getMyCollections() {
        return readCollectionService.getMyCollections();
    }

    @QueryMapping
    public List<CollectionWithCategoryAndQuizCountAndResults> getCollectionsByCategoryId(@Argument Long categoryId) {
        return readCollectionService.getCollectionsByCategoryId(categoryId);
    }
}
