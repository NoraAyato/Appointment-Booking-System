package com.abs.app.infrastructure.persistence.jpa;

import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.enums.StaffShiftStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StaffShiftJpaRepository extends JpaRepository<StaffShift, Long> {
    @EntityGraph(attributePaths = "staff")
    @Query("""
            SELECT ss
            FROM StaffShift ss
            WHERE (:keyword IS NULL OR :keyword = ''
                OR LOWER(ss.staff.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(ss.staff.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:status IS NULL OR ss.status = :status)
            """)
    Page<StaffShift> search(
            @Param("keyword") String keyword,
            @Param("status") StaffShiftStatus status,
            Pageable pageable);

    @EntityGraph(attributePaths = "staff")
    @Query("""
            SELECT ss
            FROM StaffShift ss
            WHERE ss.staff.userId = :staffId
            AND (:keyword IS NULL OR :keyword = ''
                OR LOWER(ss.staff.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(ss.staff.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:status IS NULL OR ss.status = :status)
            """)
    Page<StaffShift> searchByStaffId(
            @Param("staffId") String staffId,
            @Param("keyword") String keyword,
            @Param("status") StaffShiftStatus status,
            Pageable pageable);
}
