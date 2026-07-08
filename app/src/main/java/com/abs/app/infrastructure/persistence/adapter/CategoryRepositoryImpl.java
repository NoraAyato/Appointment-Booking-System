package com.abs.app.infrastructure.persistence.adapter;

import com.abs.app.domain.entity.Category;
import com.abs.app.domain.repository.CategoryRepository;
import com.abs.app.infrastructure.persistence.jpa.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryJpaRepository categoryJpaRepository;


    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll();
    }

    @Override
    public Page<Category> search(String keyword, Pageable pageable) {
        return categoryJpaRepository.search(keyword, pageable);
    }

    @Override
    public Optional<Category> findById(String id) {
        return categoryJpaRepository.findById(id);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryJpaRepository.findByName(name);
    }

    @Override
    public Category save(Category category) {
        return categoryJpaRepository.save(category);
    }

    @Override
    public void deleteById(String id) {
        categoryJpaRepository.deleteById(id);
    }
}
