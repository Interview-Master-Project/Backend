package com.interview_master.domain.collection;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.Access;
import com.interview_master.domain.BaseEntity;
import com.interview_master.ui.request.EditCollectionInput;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static com.interview_master.common.exception.ErrorCode.FORBIDDEN_ACCESS;
import static com.interview_master.domain.Access.PUBLIC;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Collection extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long creatorId;

    private Long categoryId;

    @Enumerated(EnumType.STRING)
    private Access access;

    protected Collection() {}

    // domain logic

    public void editCollection(EditCollectionInput editCollectionInput) {
        if (editCollectionInput.getName() != null) setName(editCollectionInput.getName());
        if (editCollectionInput.getCategoryId() != null) setCategoryId(editCollectionInput.getCategoryId());
        if (editCollectionInput.getAccess() != null) setAccess(editCollectionInput.getAccess());
    }

    public void canAccess(Long userId) {
        boolean isCreator = this.creatorId.equals(userId);
        boolean isPublic = this.getAccess().equals(PUBLIC);

        if (!isCreator && !isPublic) {
            throw new ApiException(FORBIDDEN_ACCESS, "접근 불가능함");
        }
    }

    public void checkExistence() {
        if(this.getIsDeleted()) throw new ApiException(ErrorCode.COLLECTION_NOT_FOUND);
    }

    // setter
    private void setName(String name) {
        if (name == null) throw new ApiException(ErrorCode.NULL_EXCEPTION, "no collection name");
        if (!name.trim().isEmpty()) this.name = name;
    }

    private void setAccess(Access access) {
        if (access == null) throw new ApiException(ErrorCode.NULL_EXCEPTION, "no collection access");
        this.access = access;
    }

    private void setCreatorId(Long creatorId) {
        if (creatorId == null) throw new ApiException(ErrorCode.NULL_EXCEPTION, "no creator id");
        this.creatorId = creatorId;
    }

    private void setCategoryId(Long categoryId) {
        if (categoryId == null) throw new ApiException(ErrorCode.NULL_EXCEPTION, "no category id");
        this.categoryId = categoryId;
    }
}
