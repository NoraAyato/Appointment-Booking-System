package com.abs.app.infrastructure.persistence.adapter;

import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.entity.enums.DiscountType;
import com.abs.app.domain.entity.enums.PromotionStatus;
import com.abs.app.domain.repository.PromotionRepository;
import com.abs.app.infrastructure.persistence.jpa.PromotionJpaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromotionRepositoryImpl implements PromotionRepository {
    private final PromotionJpaRepository promotionJpaRepository;

    @Override
    public List<Promotion> findAll() {
        return promotionJpaRepository.findAll();
    }

    @Override
    public Optional<Promotion> findById(String id) {
        return promotionJpaRepository.findById(id);
    }

    @Override
    public Promotion save(Promotion promotion) {
        return promotionJpaRepository.save(promotion);
    }

    @Override
    public void deleteById(String id) {
        promotionJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Promotion> findByCode(String code) {
        return promotionJpaRepository.findByCode(code);
    }

    @Override
    public List<Promotion> findAvailablePromotions(PromotionStatus status, LocalDate currentDate) {
        return promotionJpaRepository.findAvailablePromotions(status, currentDate);
    }

    @Override
    public Page<Promotion> findAvailablePromotions(
            PromotionStatus status,
            LocalDate currentDate,
            Pageable pageable) {
        return promotionJpaRepository.findAvailablePromotions(status, currentDate, pageable);
    }

    @Override
    public Page<Promotion> search(
            String keyword,
            PromotionStatus status,
            DiscountType discountType,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable) {
        return promotionJpaRepository.search(keyword, status, discountType, fromDate, toDate, pageable);
    }
}
