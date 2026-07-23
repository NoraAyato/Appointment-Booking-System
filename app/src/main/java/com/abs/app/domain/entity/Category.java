package com.abs.app.domain.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "category_id", nullable = false, columnDefinition = "VARCHAR(20)")
    private String id;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(100)")
    private String name;

    @Column(name = "tag_color", nullable = true, columnDefinition = "VARCHAR(20)")
    private String tagColor;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceEntity> services;
}
