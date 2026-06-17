package com.abs.app.infrastructure.persistence.jpa;

import com.abs.app.domain.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionJpaRepository extends JpaRepository<Promotion, String> {
    Page<Promotion> findByDescriptionContaining(String keyword, Pageable pageable);
}
