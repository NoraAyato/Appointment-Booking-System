package com.abs.app.infrastructure.persistence.adapter;

import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.repository.PromotionRepository;
import com.abs.app.infrastructure.persistence.jpa.PromotionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
