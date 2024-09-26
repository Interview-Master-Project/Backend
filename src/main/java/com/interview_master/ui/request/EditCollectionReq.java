package com.interview_master.ui.request;

import com.interview_master.domain.Access;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditCollectionReq {

  private String newName;
  private String newDescription;
  private MultipartFile image;
  private Long categoryId;
  private Access newAccess;
}
