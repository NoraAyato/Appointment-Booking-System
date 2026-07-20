package com.abs.app.infrastructure.persistence.jpa;

import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface StaffServiceJpaRepository extends JpaRepository<StaffService, Long> {
    Optional<StaffService> findByStaffUserIdAndServiceId(String staffId, String staffServiceId);
    boolean existsByStaffUserIdAndStatus(String staffId, StaffServiceStatus status);

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

    @Query("""
            SELECT DISTINCT ss
            FROM StaffService ss
            JOIN FETCH ss.staff staff
            JOIN FETCH ss.service service
            WHERE service.id = :serviceId
            AND service.status = :serviceStatus
            AND staff.status = :staffStatus
            AND ss.status = :staffServiceStatus
            AND EXISTS (
                SELECT 1
                FROM StaffShift shift
                WHERE shift.staff = staff
                AND shift.status = :staffShiftStatus
                AND shift.workDate = :date
                AND shift.startTime <= :time
                AND shift.endTime >= :requestedEndTime
            )
            AND NOT EXISTS (
                SELECT 1
                FROM BlockedSlot blockedSlot
                WHERE (blockedSlot.staff IS NULL OR blockedSlot.staff = staff)
                AND blockedSlot.status = :blockedSlotStatus
                AND (blockedSlot.blockedDate IS NULL OR blockedSlot.blockedDate = :date)
                AND (
                    (blockedSlot.startTime IS NULL AND blockedSlot.endTime IS NULL)
                    OR (
                        blockedSlot.startTime < :requestedEndTime
                        AND blockedSlot.endTime > :time
                    )
                )
            )
            AND NOT EXISTS (
                SELECT 1
                FROM AppointmentDetail appointmentDetail
                JOIN appointmentDetail.appointment appointment
                WHERE appointmentDetail.staff = staff
                AND appointment.status <> :excludedAppointmentStatus
                AND appointmentDetail.startTime < :requestedEndAt
                AND appointmentDetail.endTime > :requestedStartAt
            )
            ORDER BY staff.firstName ASC, staff.lastName ASC, staff.userId ASC
            """)
    List<StaffService> findAvailableStaffForService(
            @Param("serviceId") String serviceId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time,
            @Param("requestedEndTime") LocalTime requestedEndTime,
            @Param("requestedStartAt") LocalDateTime requestedStartAt,
            @Param("requestedEndAt") LocalDateTime requestedEndAt,
            @Param("serviceStatus") ServiceStatus serviceStatus,
            @Param("staffStatus") UserStatus staffStatus,
            @Param("staffServiceStatus") StaffServiceStatus staffServiceStatus,
            @Param("staffShiftStatus") StaffShiftStatus staffShiftStatus,
            @Param("blockedSlotStatus") BlockedSlotStatus blockedSlotStatus,
            @Param("excludedAppointmentStatus") AppointmentStatus excludedAppointmentStatus);

    @Query("""
            SELECT COUNT(DISTINCT staff.userId)
            FROM StaffService ss
            JOIN ss.staff staff
            JOIN ss.service service
            WHERE service.id = :serviceId
            AND service.status = :serviceStatus
            AND staff.status = :staffStatus
            AND ss.status = :staffServiceStatus
            AND EXISTS (
                SELECT 1
                FROM StaffShift shift
                WHERE shift.staff = staff
                AND shift.status = :staffShiftStatus
                AND shift.workDate = :date
                AND shift.startTime <= :time
                AND shift.endTime >= :requestedEndTime
            )
            AND NOT EXISTS (
                SELECT 1
                FROM BlockedSlot blockedSlot
                WHERE (blockedSlot.staff IS NULL OR blockedSlot.staff = staff)
                AND blockedSlot.status = :blockedSlotStatus
                AND (blockedSlot.blockedDate IS NULL OR blockedSlot.blockedDate = :date)
                AND (
                    (blockedSlot.startTime IS NULL AND blockedSlot.endTime IS NULL)
                    OR (
                        blockedSlot.startTime < :requestedEndTime
                        AND blockedSlot.endTime > :time
                    )
                )
            )
            AND NOT EXISTS (
                SELECT 1
                FROM AppointmentDetail appointmentDetail
                JOIN appointmentDetail.appointment appointment
                WHERE appointmentDetail.staff = staff
                AND appointment.status <> :excludedAppointmentStatus
                AND appointmentDetail.startTime < :requestedEndAt
                AND appointmentDetail.endTime > :requestedStartAt
            )
            """)
    long countAvailableStaffForService(
            @Param("serviceId") String serviceId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time,
            @Param("requestedEndTime") LocalTime requestedEndTime,
            @Param("requestedStartAt") LocalDateTime requestedStartAt,
            @Param("requestedEndAt") LocalDateTime requestedEndAt,
            @Param("serviceStatus") ServiceStatus serviceStatus,
            @Param("staffStatus") UserStatus staffStatus,
            @Param("staffServiceStatus") StaffServiceStatus staffServiceStatus,
            @Param("staffShiftStatus") StaffShiftStatus staffShiftStatus,
            @Param("blockedSlotStatus") BlockedSlotStatus blockedSlotStatus,
            @Param("excludedAppointmentStatus") AppointmentStatus excludedAppointmentStatus);

    @Query("""
            SELECT ss.staff.userId, ss.service.name
            FROM StaffService ss
            WHERE ss.staff.userId IN :staffIds
            AND ss.status = :staffServiceStatus
            AND ss.service.status = :serviceStatus
            ORDER BY ss.service.name ASC
            """)
    List<Object[]> findSpecialtiesByStaffIds(
            @Param("staffIds") List<String> staffIds,
            @Param("staffServiceStatus") StaffServiceStatus staffServiceStatus,
            @Param("serviceStatus") ServiceStatus serviceStatus);
}
