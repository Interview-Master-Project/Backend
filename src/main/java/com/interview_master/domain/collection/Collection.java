package com.interview_master.domain.collection;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.Access;
import com.interview_master.domain.BaseEntity;
import com.interview_master.domain.category.Category;
import com.interview_master.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.interview_master.common.exception.ErrorCode.FORBIDDEN_ACCESS;
import static com.interview_master.domain.Access.PUBLIC;

@Entity
@Table(name = "collections")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Collection extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    private Access access;

    // domain logic

//    public void editCollection(EditCollectionInput editCollectionInput) {
//        String newName = editCollectionInput.getNewName();
//        Long newCategoryId = editCollectionInput.getCategoryId();
//        Access newAccess = editCollectionInput.getNewAccess();
//
//        boolean nameChanged = newName != null && !newName.equals(this.name);
//        boolean categoryChanged = newCategoryId != null && !newCategoryId.equals(this.category.getId());
//        boolean accessChanged = newAccess != null && newAccess != this.access;
//
//        if (nameChanged) {
//            setName(newName);
//        }
//        if (categoryChanged) {
//            setCategory(newCategoryId);
//        }
//        if (accessChanged) {
//            setAccess(newAccess);
//        }
//    }

    public void canAccess(Long userId) {
        boolean isCreator = this.creator.getId().equals(userId);
        boolean isPublic = this.getAccess().equals(PUBLIC);

        if (!isCreator && !isPublic) {
            throw new ApiException(FORBIDDEN_ACCESS, "접근 불가능함");
        }
    }

    /**
     * collection 주인인지 검증하는 로직
     */
    public void isOwner(Long userId) {
        boolean isCreator = this.creator.getId().equals(userId);
        if (!isCreator) {
            throw new ApiException(ErrorCode.UNAUTHORIZED_COLLECTION_MODIFICATION);
        }
    }

    // setter
    private void setName(String name) {
        this.name = name;
    }

    private void setAccess(Access access) {
        this.access = access;
    }

    private void setCreator(User creator) {
        this.creator = creator ;
    }

    private void setCategory(Category category) {
        this.category = category;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    private void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
