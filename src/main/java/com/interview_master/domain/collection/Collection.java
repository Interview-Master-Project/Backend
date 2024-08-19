package com.interview_master.domain.collection;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.Access;
import com.interview_master.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import static com.interview_master.common.exception.ErrorCode.FORBIDDEN_ACCESS;
import static com.interview_master.domain.Access.PUBLIC;

@Entity
@Getter
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

    public Collection(String name, Long creatorId, Long categoryId, Access access) {
        setCategoryId(categoryId);
        setName(name);
        setCreatorId(creatorId);
        setAccess(access);
    }

    // domain logic

    public void editCollection(EditCollectionInput editCollectionInput) {
        if (editCollectionInput.name() != null) setName(editCollectionInput.name());
        if (editCollectionInput.categoryId() != null) setCategoryId(editCollectionInput.categoryId());
        if (editCollectionInput.access() != null) setAccess(editCollectionInput.access());
    }

    public void canAccess(Long userId) {
        boolean isCreator = this.creatorId.equals(userId);
        boolean isPublic = this.getAccess().equals(PUBLIC);

        if (!isCreator && !isPublic) {
            throw new ApiException(FORBIDDEN_ACCESS, "접근 불가능함");
        }
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
