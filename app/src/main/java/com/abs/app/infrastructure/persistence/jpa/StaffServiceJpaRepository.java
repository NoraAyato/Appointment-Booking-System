package com.abs.app.infrastructure.persistence.jpa;

import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.enums.StaffServiceStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StaffServiceJpaRepository extends JpaRepository<StaffService, Long> {
    Optional<StaffService> findByStaffUserIdAndServiceId(String staffId, String staffServiceId);

    @EntityGraph(attributePaths = { "staff", "service" })
    @Query("""
            SELECT ss
            FROM StaffService ss
            WHERE (:keyword IS NULL OR :keyword = ''
                OR LOWER(ss.staff.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(ss.staff.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:status IS NULL OR ss.status = :status)
            """)
    Page<StaffService> search(
            @Param("keyword") String keyword,
            @Param("status") StaffServiceStatus status,
            Pageable pageable);
}
