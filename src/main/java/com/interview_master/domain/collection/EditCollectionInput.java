package com.interview_master.domain.collection;

import com.interview_master.domain.Access;
import lombok.Getter;

@Getter
public class EditCollectionInput {

    private String newName;
    private Long categoryId;
    private Access newAccess;
}
