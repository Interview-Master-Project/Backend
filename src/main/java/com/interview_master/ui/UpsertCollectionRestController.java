package com.interview_master.ui;

import com.interview_master.application.UpsertCollectionService;
import com.interview_master.ui.request.CreateCollectionReq;
import com.interview_master.ui.request.EditCollectionReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UpsertCollectionRestController {

  private final UpsertCollectionService upsertCollectionService;

  @PostMapping("/api/collections")
  public ResponseEntity<String> createCollection(
      @ModelAttribute @Valid CreateCollectionReq createCollectionReq) {
    log.info("Received createCollection request: {}", createCollectionReq);

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
