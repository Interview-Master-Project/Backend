package com.interview_master.ui;

import com.interview_master.ui.response.CollectionSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CollectionSearchController {


    @QueryMapping
    public CollectionSearchResult searchCollections(
            @Argument List<Long> categoryIds, @Argument List<String> keywords,
            @Argument Integer maxCorrectRate, @ContextValue(required = false) Long userId) {

        // 비회원 접근 -> PUBLIC인 컬렉션만 리턴
        if (userId == null) {

        } else {    // 회원 접근 -> 다른 유저 PUBLIC + 내 컬렉션 리턴

        }

        return new CollectionSearchResult();
    }
}
