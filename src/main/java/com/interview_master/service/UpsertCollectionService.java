package com.interview_master.service;

import static com.interview_master.common.constant.Constant.IMAGE_DELETE_TOPIC;

import com.interview_master.common.constant.Constant;
import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.category.Category;
import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.collectionlike.CollectionsLikes;
import com.interview_master.domain.user.User;
import com.interview_master.infrastructure.CategoryRepository;
import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.CollectionsLikesRepository;
import com.interview_master.infrastructure.UserRepository;
import com.interview_master.kafka.producer.CollectionProducer;
import com.interview_master.kafka.producer.ImageDeleteProducer;
import com.interview_master.resolver.request.CreateCollectionReq;
import com.interview_master.resolver.request.EditCollectionReq;
import com.interview_master.resolver.response.EditCollectionRes;
import com.interview_master.util.ExtractUserId;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UpsertCollectionService {

  private final CollectionRepository collectionRepository;
  private final UserRepository userRepository;
  private final NcpImageService imageService;
  private final CategoryRepository categoryRepository;
  private final CollectionsLikesRepository collectionsLikesRepository;

  private final CollectionProducer collectionProducer;
  private final ImageDeleteProducer imageProducer;

  public Collection saveCollection(CreateCollectionReq createCollectionReq) {
    // token의 userId 가져오기
    Long userId = ExtractUserId.extractUserIdFromContextHolder();

    // 이미지 저장
    String imgUrl = "";
    if (createCollectionReq.getImage() != null) {
      imgUrl = imageService.uploadImage(createCollectionReq.getImage());
    }

    // 카테고리 존재 여부 검증
    Category category = categoryRepository.findById(createCollectionReq.getCategoryId())
        .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));

    // 유저 존재 여부 검증
    User user = userRepository.findByIdAndIsDeletedFalse(userId)
        .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

    // 컬렉션 생성
    Collection newCollection = Collection.builder()
        .name(createCollectionReq.getName())
        .description(createCollectionReq.getDescription())
        .likes(0)
        .access(createCollectionReq.getAccess())
        .category(category)
        .imgUrl(imgUrl)
        .creator(user)
        .build();

    // 컬렉션 저장
    return collectionRepository.save(newCollection);
  }

  public EditCollectionRes editCollection(Long collectionId, EditCollectionReq editReq) {
    Long userId = ExtractUserId.extractUserIdFromContextHolder();

    Collection collection = collectionRepository.findByIdAndIsDeletedFalse(collectionId)
        .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));

    // 수정 가능한지 검증
    collection.isOwner(userId);

    // image 입력받았으면 기존 이미지 삭제하고 새로운 이미지 업로드
    String newImgUrl = null;
    if (editReq.getImage() != null) {
      // 기존 이미지 삭제 -> 비동기 처리
      imageProducer.delete(IMAGE_DELETE_TOPIC, collection.getImgUrl());
      newImgUrl = imageService.uploadImage(editReq.getImage());
    }

    Category newCategory = collection.getCategory();
    if (editReq.getCategoryId() != null) {
      newCategory = categoryRepository.findById(editReq.getCategoryId())
          .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    collection.edit(editReq.getNewName(), editReq.getNewDescription(), newImgUrl, newCategory,
        editReq.getNewAccess());

    return EditCollectionRes.from(collection);
  }

  public void deleteCollection(Long collectionId, Long userId) {
    Collection collection = collectionRepository.findByIdWithQuizzes(collectionId)
        .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));

    collection.isOwner(userId);

    collection.markDeleted();

    // 컬렉션의 이미지 삭제를 위한 메시지 발행
    if (collection.getImgUrl() != null) imageProducer.delete(IMAGE_DELETE_TOPIC, collection.getImgUrl());

    // 컬렉션에 속한 퀴즈들 삭제 처리를 위한 메시지 발행
    collectionProducer.delete(Constant.COLLECTION_DELETE_TOPIC, collection.getId());
  }

  public void likeCollection(Long collectionId, Long userId) {
    // 이미 좋아요 눌렀었는지 검증
    boolean exists = collectionsLikesRepository.existsByCollectionIdAndUserId(
        collectionId, userId);
    if (exists) {
      throw new ApiException(ErrorCode.ALREADY_LIKED);
    }

    Collection collection = collectionRepository.findWithLockByIdAndIsDeletedFalse(collectionId)
        .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));

    User user = userRepository.findByIdAndIsDeletedFalse(userId)
        .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

    collection.canAccess(userId);

    collectionsLikesRepository.save(
        CollectionsLikes.builder()
            .collection(collection)
            .user(user)
            .createdAt(LocalDateTime.now())
            .build()
    );
    collection.like();
  }

  public void unlikeCollection(Long collectionId, Long userId) {
    CollectionsLikes collectionLike = collectionsLikesRepository.findByCollectionIdAndUserId(
            collectionId, userId)
        .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_LIKE_NOT_FOUND));

    Collection collection = collectionRepository.findWithLockByIdAndIsDeletedFalse(collectionId)
        .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));

    // 정상적인 경우(좋아요 누른 거에 대한 취소 요청)
    collectionsLikesRepository.delete(collectionLike);
    collection.unlike();
  }
}
