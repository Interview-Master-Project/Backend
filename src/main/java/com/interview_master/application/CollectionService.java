package com.interview_master.application;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.Access;
import com.interview_master.domain.collection.Collection;
import com.interview_master.infrastructure.CategoryRepository;
import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.ui.request.CreateCollectionInput;
import com.interview_master.ui.request.EditCollectionInput;
import com.interview_master.util.ExtractUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.interview_master.domain.Access.PUBLIC;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final CategoryRepository categoryRepository;

    private final PermissionService permissionService;

    @Transactional
    public void createCollection(CreateCollectionInput input) {
        Long currentUserId = ExtractUserId.extractUserIdFromContextHolder();
        Long categoryId = input.getCategoryId();

        // default 설정
        if (categoryId != null) checkCategoryExist(categoryId);
        else categoryId = 999L;

        Access access = input.getAccess() != null ? input.getAccess() : PUBLIC;


        Collection newCollection = Collection.builder()
                .name(input.getName())
                .categoryId(categoryId)
                .access(access)
                .creatorId(currentUserId)
                .build();

        collectionRepository.save(newCollection);
    }

    @Transactional
    public void editCollection(Long collectionId, EditCollectionInput input) {
        Long currentUserId = ExtractUserId.extractUserIdFromContextHolder();

        checkCategoryExist(input.getCategoryId());

        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));

        collection.checkExistence();

        permissionService.checkEditPermission(currentUserId, collection);

        collection.editCollection(input);
    }


    private void checkCategoryExist(Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
