package com.abs.app.domain.repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.entity.enums.DiscountType;
import com.abs.app.domain.entity.enums.PromotionStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PromotionRepository {

    Optional<Promotion> findById(String id);

    List<Promotion> findAll();

    Promotion save(Promotion promotion);

    void deleteById(String id);

    Optional<Promotion> findByCode(String code);

    Page<Promotion> search(
            String keyword,
            PromotionStatus status,
            DiscountType discountType,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable);

}
