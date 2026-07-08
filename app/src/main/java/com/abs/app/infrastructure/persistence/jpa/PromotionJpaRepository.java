package com.abs.app.infrastructure.persistence.jpa;

import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.entity.enums.DiscountType;
import com.abs.app.domain.entity.enums.PromotionStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PromotionJpaRepository extends JpaRepository<Promotion, String> {
    Optional<Promotion> findByCode(String code);

    @Query("""
            SELECT p
            FROM Promotion p
            WHERE (:keyword IS NULL OR :keyword = ''
                OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(p.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:status IS NULL OR p.status = :status)
            AND (:discountType IS NULL OR p.discountType = :discountType)
            AND (:fromDate IS NULL OR p.endDate >= :fromDate)
            AND (:toDate IS NULL OR p.startDate <= :toDate)
            """)
    Page<Promotion> search(
            @Param("keyword") String keyword,
            @Param("status") PromotionStatus status,
            @Param("discountType") DiscountType discountType,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable);
}
