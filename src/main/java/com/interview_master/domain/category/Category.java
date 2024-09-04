package com.interview_master.domain.category;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "categories")
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    protected Category() {}

    public Category(String name) {
        setName(name);
    }

    private void setName(String name) {
        this.name = name;
    }
}
