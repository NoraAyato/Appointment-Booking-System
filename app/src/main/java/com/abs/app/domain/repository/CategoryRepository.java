package com.abs.app.domain.repository;

import com.abs.app.domain.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<Category> findAll();
    Optional<Category> findById(String id);
    Optional<Category> findByName(String name);
    Category save(Category category);
    void deleteById(String id);
}
