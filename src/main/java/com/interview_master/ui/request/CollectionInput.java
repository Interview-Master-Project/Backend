package com.interview_master.ui.request;

import com.interview_master.domain.Access;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionInput {

    @NotEmpty(message = "컬렉션 이름은 필수 입력입니다.")
    private String name;
    private Long categoryId;
    private Access access;
}
