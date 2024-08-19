package com.interview_master.application;

import com.interview_master.infrastructure.CollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;

}
