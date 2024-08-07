package com.interview_master.domain.collection;

import com.interview_master.domain.Access;
import lombok.Getter;

@Getter
public class EditCollectionDTO {
    private CollectionName newName;
    private Access newAccess;
}
