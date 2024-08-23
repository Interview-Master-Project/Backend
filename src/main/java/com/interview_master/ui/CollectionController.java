package com.interview_master.ui;

import com.interview_master.application.CollectionService;
import com.interview_master.ui.request.CollectionInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @MutationMapping
    public String createCollection(@Valid @Argument CollectionInput collectionInput) {
        collectionService.createCollection(collectionInput);

        return "컬렉션 생성에 성공했습니다!";
    }
}
