package com.interview_master.ui.request;

import com.interview_master.domain.Access;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCollectionInput {

    private String name;

    private String description;

    private Access access;

    private Long categoryId;
}