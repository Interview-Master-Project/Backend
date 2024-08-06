package com.interview_master.domain.collection;

import com.interview_master.domain.Access;
import com.interview_master.domain.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class Collection extends BaseEntity {
    @EmbeddedId
    private CollectionId id;

    @Embedded
    private CollectionName name;

    @Enumerated(EnumType.STRING)
    private Access access;

    protected Collection() {}

    public Collection(CollectionName name, Access access) {
        setName(name);
        setAccess(access);
    }

    // domain logic

    public void editCollection(EditCollectionDTO editCollectionDTO) {
        CollectionName newName = editCollectionDTO.getNewName();
        Access newAccess = editCollectionDTO.getNewAccess();

        boolean nameChanged = newName != null && !newName.equals(this.name);
        boolean accessChanged = newAccess != null && newAccess != this.access;

        if (nameChanged) {
            setName(newName);
        }
        if (accessChanged) {
            setAccess(newAccess);
        }
    }

    private void setName(CollectionName name) {
        this.name = name;
    }

    private void setAccess(Access access) {
        this.access = access;
    }
}
