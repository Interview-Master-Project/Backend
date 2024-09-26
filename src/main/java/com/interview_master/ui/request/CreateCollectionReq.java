package com.interview_master.ui.request;

import com.interview_master.domain.Access;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCollectionReq {

  private MultipartFile image;

  @NotEmpty(message = "컬렉션 이름은 필수 값입니다.")
  private String name;

  @NotEmpty(message = "컬렉션 설명은 필수 값입니다.")
  private String description;

  @NotNull(message = "공개 범위 설정은 필수입니다.")
  private Access access;

  @NotNull(message = "카테고리 선택은 필수입니다.")
  private Long categoryId;
}