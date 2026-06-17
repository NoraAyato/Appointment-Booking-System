package com.abs.app.domain.repository;

import com.abs.app.domain.entity.Promotion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PromotionRepository {
    List<Promotion> findAll();
    List<Promotion> searchPromotions(String keyword, LocalDate startDate, LocalDate endDate, Boolean active);
    Optional<Promotion> findById(String id);
    Promotion save(Promotion promotion);
    void deleteById(String id);
}
