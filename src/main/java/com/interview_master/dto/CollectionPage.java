package com.interview_master.dto;

import com.interview_master.domain.collection.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionPage {

  private List<Collection> collections;
  private PageInfo pageInfo;
  private long totalCount;
}
