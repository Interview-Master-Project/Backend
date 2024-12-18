package com.interview_master.resolver;

import com.interview_master.resolver.response.EditCollectionRes;
import com.interview_master.service.UpsertCollectionService;
import com.interview_master.domain.collection.Collection;
import com.interview_master.resolver.request.CreateCollectionReq;
import com.interview_master.resolver.request.EditCollectionReq;
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
  public ResponseEntity<Collection> createCollection(
      @ModelAttribute @Valid CreateCollectionReq createCollectionReq) {
    log.info("Received createCollection request: {}", createCollectionReq);

    return ResponseEntity.ok(upsertCollectionService.saveCollection(createCollectionReq));
  }

  @PatchMapping("/api/collections/{collectionId}")
  public ResponseEntity<EditCollectionRes> editCollection(@PathVariable Long collectionId,
      @ModelAttribute EditCollectionReq editCollectionReq) {

    return ResponseEntity.ok(upsertCollectionService.editCollection(collectionId, editCollectionReq));
  }
}
