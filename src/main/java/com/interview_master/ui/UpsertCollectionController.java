package com.interview_master.ui;

import com.interview_master.application.UpsertCollectionService;
import com.interview_master.ui.request.CreateCollectionReq;
import com.interview_master.ui.request.EditCollectionReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UpsertCollectionController {

    private final UpsertCollectionService upsertCollectionService;

    @PostMapping("/api/collections")
    public ResponseEntity<String> createCollection(@ModelAttribute @Valid CreateCollectionReq createCollectionReq) {
        upsertCollectionService.saveCollection(createCollectionReq);

        return ResponseEntity.ok("success");
    }

    @PatchMapping("/api/collections/{collectionId}")
    public ResponseEntity<String> editCollection(@PathVariable Long collectionId,
                                                 @ModelAttribute EditCollectionReq editCollectionReq) {
        upsertCollectionService.editCollection(collectionId, editCollectionReq);
        return ResponseEntity.ok("success");
    }
}
