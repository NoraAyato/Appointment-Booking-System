package com.abs.app.infrastructure.persistence.jpa;

import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.entity.enums.UserStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

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

    @EntityGraph(attributePaths = "staff")
    @Query("""
            SELECT DISTINCT shift
            FROM StaffShift shift
            JOIN shift.staff staff
            JOIN staff.staffServices staffService
            JOIN staffService.service service
            WHERE service.id = :serviceId
            AND service.status = :serviceStatus
            AND staff.status = :staffStatus
            AND staffService.status = :staffServiceStatus
            AND shift.status = :staffShiftStatus
            AND shift.workDate = :date
            ORDER BY shift.startTime ASC, shift.endTime ASC
            """)
    List<StaffShift> findApprovedShiftsForService(
            @Param("serviceId") String serviceId,
            @Param("date") LocalDate date,
            @Param("serviceStatus") ServiceStatus serviceStatus,
            @Param("staffStatus") UserStatus staffStatus,
            @Param("staffServiceStatus") StaffServiceStatus staffServiceStatus,
            @Param("staffShiftStatus") StaffShiftStatus staffShiftStatus);

    long countByStatus(StaffShiftStatus status);
}
