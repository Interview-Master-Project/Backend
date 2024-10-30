package com.interview_master.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {

  public static final String ACCESS_TOKEN_KEY = "Authorization";

  public static final String USER_ID = "userID";

  public static final String IMAGE_DIRECTORY_NAME = "collection";

  public static final String DIRECTORY_SEPARATOR = "/";

  public static final String SORT_LATEST = "createdAt";

  public static final String SORT_LOWACCURACY = "accuracy";
  public static final String SORT_MOSTLIKED = "liked";

}
