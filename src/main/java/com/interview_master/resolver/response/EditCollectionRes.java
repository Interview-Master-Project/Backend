package com.interview_master.resolver.response;

import com.interview_master.domain.Access;
import com.interview_master.domain.collection.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditCollectionRes {
  private Long id;
  private String name;
  private String description;
  private String imgUrl;
  private Integer likes;
  private Access access;
  private String categoryName;
  private Long creatorId;
  private String creatorName;

  public static EditCollectionRes from(Collection collection) {
    return EditCollectionRes.builder()
        .id(collection.getId())
        .name(collection.getName())
        .description(collection.getDescription())
        .imgUrl(collection.getImgUrl())
        .likes(collection.getLikes())
        .access(collection.getAccess())
        .categoryName(collection.getCategory().getName())
        .creatorId(collection.getCreator().getId())
        .creatorName(collection.getCreator().getNickname())
        .build();
  }
}
