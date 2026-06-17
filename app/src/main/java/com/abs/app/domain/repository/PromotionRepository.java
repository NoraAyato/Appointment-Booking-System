package com.abs.app.domain.repository;

import com.abs.app.domain.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PromotionRepository {
    List<Promotion> findAll();
    Page<Promotion> findAll(Pageable pageable);
    Page<Promotion> findByDescriptionContaining(String keyword, Pageable pageable);
    Optional<Promotion> findById(String id);
    Promotion save(Promotion promotion);
    void deleteById(String id);
}
