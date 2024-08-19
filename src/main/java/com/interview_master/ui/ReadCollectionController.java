package com.interview_master.ui;

import com.interview_master.application.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ReadCollectionController {

    private final CollectionService collectionService;

}
