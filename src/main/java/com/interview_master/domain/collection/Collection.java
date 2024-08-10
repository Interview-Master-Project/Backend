package com.interview_master.domain.collection;

import com.interview_master.domain.Access;
import com.interview_master.domain.BaseEntity;
import jakarta.persistence.*;

@Entity
public class Collection extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private CollectionName name;

    private Long creatorId;

    @Enumerated(EnumType.STRING)
    private Access access;

    protected Collection() {}

    public Collection(CollectionName name, Long creatorId, Access access) {
        setName(name);
        setCreatorId(creatorId);
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

    private void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
}
