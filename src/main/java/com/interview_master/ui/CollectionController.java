package com.interview_master.ui;

import com.interview_master.application.CollectionService;
import com.interview_master.ui.request.CreateCollectionInput;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @MutationMapping
    public String createCollection(@Argument CreateCollectionInput createCollectionInput) {
        collectionService.createCollection(createCollectionInput);

        return "컬렉션 생성에 성공했습니다!";
    }
}
