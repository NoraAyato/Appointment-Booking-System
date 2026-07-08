package com.abs.app.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;

public interface BlockedSlotJpaRepository extends JpaRepository<BlockedSlot, Long> {
    @EntityGraph(attributePaths = "staff")
    @Query("""
            SELECT bs
            FROM BlockedSlot bs
            WHERE (:keyword IS NULL OR :keyword = ''
                OR LOWER(bs.reason) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(bs.staff.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(bs.staff.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:status IS NULL OR bs.status = :status)
            """)
    Page<BlockedSlot> search(
            @Param("keyword") String keyword,
            @Param("status") BlockedSlotStatus status,
            Pageable pageable);

    @EntityGraph(attributePaths = "staff")
    @Query("""
            SELECT bs
            FROM BlockedSlot bs
            WHERE bs.staff.userId = :staffId
            AND (:keyword IS NULL OR :keyword = ''
                OR LOWER(bs.reason) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:status IS NULL OR bs.status = :status)
            """)
    Page<BlockedSlot> searchByStaffId(
            @Param("staffId") String staffId,
            @Param("keyword") String keyword,
            @Param("status") BlockedSlotStatus status,
            Pageable pageable);
}
