package com.interview_master.domain.collection;

import com.interview_master.domain.Access;

public record EditCollectionInput (
        String name,
        Long categoryId,
        Access access) {
}
