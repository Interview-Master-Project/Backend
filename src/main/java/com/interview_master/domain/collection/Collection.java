package com.interview_master.domain.collection;

import com.interview_master.domain.Access;
import com.interview_master.domain.BaseEntity;
import jakarta.persistence.*;

@Entity
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
        String newName = editCollectionInput.getNewName();
        Long newCategoryId = editCollectionInput.getCategoryId();
        Access newAccess = editCollectionInput.getNewAccess();

        boolean nameChanged = newName != null && !newName.equals(this.name);
        boolean categoryChanged = newCategoryId != null && !newCategoryId.equals(this.categoryId);
        boolean accessChanged = newAccess != null && newAccess != this.access;

        if (nameChanged) {
            setName(newName);
        }
        if (categoryChanged) {
            setCategoryId(newCategoryId);
        }
        if (accessChanged) {
            setAccess(newAccess);
        }
    }

    // setter
    private void setName(String name) {
        this.name = name;
    }

    private void setAccess(Access access) {
        this.access = access;
    }

    private void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    private void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
