package com.interview_master.ui.response;


import com.interview_master.dto.CollectionWithAttempts;
import com.interview_master.dto.PageInfo;
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

    private List<CollectionWithAttempts> collectionWithAttempts;

    private PageInfo pageInfo;
}
