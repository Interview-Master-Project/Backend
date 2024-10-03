package com.interview_master.service;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.category.Category;
import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.user.User;
import com.interview_master.infrastructure.CategoryRepository;
import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.UserRepository;
import com.interview_master.ui.request.CreateCollectionReq;
import com.interview_master.ui.request.EditCollectionReq;
import com.interview_master.util.ExtractUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpsertCollectionService {

  private final CollectionRepository collectionRepository;
  private final UserRepository userRepository;
  private final NcpImageService imageService;
  private final CategoryRepository categoryRepository;

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
        .access(createCollectionReq.getAccess())
        .category(category)
        .imgUrl(imgUrl)
        .creator(user)
        .build();

    // 컬렉션 저장
    return collectionRepository.save(newCollection);
  }

  public Collection editCollection(Long collectionId, EditCollectionReq editReq) {
    Long userId = ExtractUserId.extractUserIdFromContextHolder();

    Collection collection = collectionRepository.findByIdWithQuizzes(collectionId)
        .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));

    // 수정 가능한지 검증
    collection.isOwner(userId);

    // image 입력받았으면 기존 이미지 삭제하고 새로운 이미지 업로드
    String newImgUrl = null;
    if (editReq.getImage() != null) {
      imageService.deleteImageFromBucket(collection.getImgUrl()); // 이미지 삭제 -> 비동기 처리 ..?
      newImgUrl = imageService.uploadImage(editReq.getImage());
    }

    Category newCategory = collection.getCategory();
    if (editReq.getCategoryId() != null) {
      newCategory = categoryRepository.findById(editReq.getCategoryId())
          .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    collection.edit(editReq.getNewName(), editReq.getNewDescription(), newImgUrl, newCategory,
        editReq.getNewAccess());

    return collection;
  }

  public void deleteCollection(Long collectionId, Long userId) {
    Collection collection = collectionRepository.findByIdWithQuizzes(collectionId)
        .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));

    collection.isOwner(userId);

    collection.markDeleted();
  }
}
