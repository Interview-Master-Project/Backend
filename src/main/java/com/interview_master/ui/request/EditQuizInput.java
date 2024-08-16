package com.interview_master.ui.request;

import com.interview_master.domain.Access;
import lombok.Getter;

public record EditQuizInput(String newQuestion, String newAnswer, Long newCollectionId, Access newAccess) {

}
