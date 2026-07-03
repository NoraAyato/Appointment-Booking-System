package com.abs.app.infrastructure.persistence.jpa;

import com.abs.app.domain.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionJpaRepository extends JpaRepository<Promotion, String> {
    Optional<Promotion> findByCode(String code);
}
