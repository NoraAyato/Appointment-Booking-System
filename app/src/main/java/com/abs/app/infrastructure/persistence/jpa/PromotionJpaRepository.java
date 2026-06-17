package com.abs.app.infrastructure.persistence.jpa;

import com.abs.app.domain.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromotionJpaRepository extends JpaRepository<Promotion, String> {
    @Query("SELECT p FROM Promotion p WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR p.description LIKE CONCAT('%', :keyword, '%')) AND " +
           "(:startDate IS NULL OR p.startDate >= :startDate) AND " +
           "(:endDate IS NULL OR p.endDate <= :endDate) AND " +
           "(:active IS NULL OR p.active = :active)")
    List<Promotion> searchPromotions(@Param("keyword") String keyword, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("active") Boolean active);
}
