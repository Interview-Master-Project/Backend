package com.interview_master.domain.quiz;

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

    @Column(name = "collection_id")
    private Long id;

    @Column(name = "collection_name")
    private String name;

}
