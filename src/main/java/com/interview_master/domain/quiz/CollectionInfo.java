package com.interview_master.domain.quiz;

import com.interview_master.domain.collection.CollectionId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CollectionInfo {

    @AttributeOverrides(
        @AttributeOverride(name = "id", column = @Column(name = "collection_id"))
    )
    private CollectionId id;

    @Column(name = "collection_name")
    private String name;

}
