package com.interview_master.ui;

import com.interview_master.domain.Access;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class QuizRequest {

    @NotNull(message = "컬렉션 선택은 필수 입니다.")
    private Long collectionId;

    @NotBlank(message = "컬렉션 선택은 필수 입니다.")
    private String collectionName;

    @NotBlank(message = "질문 입력은 필수 입니다.")
    private String question;

    @NotBlank(message = "답변 입력은 필수 입니다.")
    private String answer;

    @NotNull(message = "공개 여부 선택은 필수 입니다.")
    private Access access;
}
