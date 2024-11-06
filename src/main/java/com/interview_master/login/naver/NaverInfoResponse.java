package com.interview_master.login.naver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.interview_master.login.OAuthInfoResponse;
import com.interview_master.login.OAuthProvider;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverInfoResponse implements OAuthInfoResponse {

  @JsonProperty("response")
  private Response response;

  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  static class Response {

    private String email;
    private String nickname;
    private String profileImage;
  }

  @Override
  public String getEmail() {
    return response.email;
  }

  @Override
  public String getNickname() {
    return response.nickname;
  }

  @Override
  public OAuthProvider getOAuthProvider() {
    return OAuthProvider.NAVER;
  }

  @Override
  public String getImageUrl() {
    return response.profileImage;
  }
}