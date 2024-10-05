package com.interview_master.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionWithAttemptsPaging {

    private List<CollectionWithAttempt> collectionsWithAttempt;

    private PageInfo pageInfo;
}
