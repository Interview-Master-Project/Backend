package com.interview_master.application;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.category.Category;
import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.user.User;
import com.interview_master.infrastructure.CategoryRepository;
import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.UserRepository;
import com.interview_master.ui.request.CreateCollectionReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final NcpImageService imageService;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Collection saveCollection(CreateCollectionReq createCollectionReq, Long userId) {
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
}
