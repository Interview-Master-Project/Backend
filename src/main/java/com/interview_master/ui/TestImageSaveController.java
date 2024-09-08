package com.interview_master.ui;

import com.interview_master.application.NcpImageService;
import com.interview_master.ui.request.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestImageSaveController {
    private final NcpImageService imageService;

    @PostMapping("api/image/save")
    public ResponseEntity<String> saveImage(@ModelAttribute Image image) {
        return ResponseEntity.ok(imageService.uploadImage(image.getImage()));
    }
}