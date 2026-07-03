package com.abs.app.domain.repository;

import java.util.List;
import java.util.Optional;

import com.abs.app.domain.entity.Promotion;

public interface PromotionRepository {

    Optional<Promotion> findById(String id);

    List<Promotion> findAll();

    Promotion save(Promotion promotion);

    void deleteById(String id);

    Optional<Promotion> findByCode(String code);

}
