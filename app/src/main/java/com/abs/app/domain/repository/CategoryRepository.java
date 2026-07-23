package com.abs.app.domain.repository;

import com.abs.app.domain.entity.Category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepository {
    List<Category> findAll();
    Page<Category> search(String keyword, Pageable pageable);
    Optional<Category> findById(String id);
    Optional<Category> findByName(String name);
    Category save(Category category);
    void deleteById(String id);
}
