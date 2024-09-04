package com.interview_master.ui.response;

import lombok.Builder;

@Builder
public class LoginRes {
    private String nickname;
    private Long userId;
}
