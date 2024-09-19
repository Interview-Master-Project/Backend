package com.interview_master.ui;

import com.interview_master.application.UpsertCollectionService;
import com.interview_master.ui.request.CreateCollectionInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CreateCollectionController {
    public final UpsertCollectionService collectionService;

    @MutationMapping
    public String createCollection(
            @Argument CreateCollectionInput createCollectionInput,
            @Argument MultipartFile image,
            @ContextValue Long userId) {
        log.info("image file name : {}\tuserId : {}", image.getOriginalFilename(), userId);

        collectionService.saveCollection(createCollectionInput, image, userId);

        return "success";
    }
}
