package com.interview_master.resolver.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditUserReq {

  private MultipartFile image;

  private String name;

  /**
   * 이미지만 삭제하는 경우 true
   */
  private Boolean deleteImageOnly;
}