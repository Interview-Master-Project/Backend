package com.interview_master.ui.request;

import com.interview_master.domain.Access;

public record EditQuizInput(
        String question,
        String answer,
        Long collectionId,
        Access access) {

}
