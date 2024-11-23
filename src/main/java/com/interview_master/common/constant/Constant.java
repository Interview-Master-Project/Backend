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

  // kafka
  public static final String USER_DELETE_TOPIC = "user-delete";
  public static final String USER_GROUP_ID = "user";
  public static final String COLLECTION_DELETE_TOPIC = "collection-delete";
  public static final String COLLECTION_GROUP_ID = "collection";
  public static final String QUIZ_DELETE_TOPIC = "quiz-delete";
  public static final String QUIZ_GROUP_ID = "quiz";
  public static final String COLLECTION_ATTEMPT_DELETE_TOPIC = "collectionAttempt-delete";
  public static final String COLLECTION_ATTEMPT_GROUP_ID = "collectionAttempt";
  public static final String IMAGE_DELETE_TOPIC = "image-delete";
  public static final String IMAGE_GROUP_ID = "image";
}
