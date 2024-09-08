package com.interview_master.ui;

import com.interview_master.application.CollectionService;
import com.interview_master.ui.request.CreateCollectionReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CreateCollectionController {

    private final CollectionService collectionService;

    @PostMapping("/api/collections")
    public ResponseEntity<String> createCollection(@ModelAttribute @Valid CreateCollectionReq createCollectionReq) {
        // TODO : token에서 userId 추출하기

        collectionService.saveCollection(createCollectionReq, 2L);

        return ResponseEntity.ok("success");
    }
}
