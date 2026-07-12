package com.abs.app.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public interface BlockedSlotJpaRepository extends JpaRepository<BlockedSlot, Long> {
    @EntityGraph(attributePaths = "staff")
    @Query("""
            SELECT bs
            FROM BlockedSlot bs
            LEFT JOIN bs.staff staff
            WHERE (:keyword IS NULL OR :keyword = ''
                OR LOWER(bs.reason) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(staff.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(staff.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))
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

    @Query("""
            SELECT COUNT(bs) > 0
            FROM BlockedSlot bs
            WHERE bs.status <> :excludedStatus
            AND (:staffId IS NULL OR bs.staff IS NULL OR bs.staff.userId = :staffId)
            AND (:blockedDate IS NULL OR bs.blockedDate IS NULL OR bs.blockedDate = :blockedDate)
            AND (
                (:startTime IS NULL AND :endTime IS NULL)
                OR (bs.startTime IS NULL AND bs.endTime IS NULL)
                OR (bs.startTime < :endTime AND bs.endTime > :startTime)
            )
            """)
    boolean existsOverlapping(
            @Param("staffId") String staffId,
            @Param("blockedDate") LocalDate blockedDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludedStatus") BlockedSlotStatus excludedStatus);

    @Query("""
            SELECT COUNT(bs) > 0
            FROM BlockedSlot bs
            WHERE bs.status = :status
            AND (:staffId IS NULL OR bs.staff IS NULL OR bs.staff.userId = :staffId)
            AND (:blockedDate IS NULL OR bs.blockedDate IS NULL OR bs.blockedDate = :blockedDate)
            AND (
                (:startTime IS NULL AND :endTime IS NULL)
                OR (bs.startTime IS NULL AND bs.endTime IS NULL)
                OR (bs.startTime < :endTime AND bs.endTime > :startTime)
            )
            """)
    boolean existsOverlappingByStatus(
            @Param("staffId") String staffId,
            @Param("blockedDate") LocalDate blockedDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("status") BlockedSlotStatus status);

    @Query("""
            SELECT COUNT(bs) > 0
            FROM BlockedSlot bs
            WHERE bs.status = :status
            AND (bs.staff IS NULL OR bs.staff.userId = :staffId)
            AND bs.blockedDate = :blockedDate
            AND bs.startTime IS NULL
            AND bs.endTime IS NULL
            """)
    boolean existsAllDayOnDateByStatus(
            @Param("staffId") String staffId,
            @Param("blockedDate") LocalDate blockedDate,
            @Param("status") BlockedSlotStatus status);

    long countByStatus(BlockedSlotStatus status);
}
