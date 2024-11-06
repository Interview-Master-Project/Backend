package com.interview_master.resolver;

import com.interview_master.domain.user.User;
import com.interview_master.resolver.request.EditUserReq;
import com.interview_master.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class EditUserRestController {

  private final UserProfileService userProfileService;

  @PatchMapping()
  public User edit(@ModelAttribute EditUserReq editUserReq) {
    return userProfileService.editProfile(editUserReq);
  }

}