package com.abs.app.domain.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.enums.AppointmentStatus;

public interface AppointmentDetailRepository {
    Map<String, Integer> countCompletedServicesByStaffIds(
            List<String> staffIds,
            AppointmentStatus appointmentStatus);

    List<Object[]> countDailyAppointments(
            LocalDateTime startAt,
            LocalDateTime endAt,
            AppointmentStatus completedStatus,
            AppointmentStatus cancelledStatus);

    List<Object[]> findTopServices(
            LocalDateTime startAt,
            LocalDateTime endAt,
            Pageable pageable);

    List<Object[]> findStaffPerformance(
            LocalDateTime startAt,
            LocalDateTime endAt,
            LocalDate fromDate,
            LocalDate toDate,
            AppointmentStatus completedStatus,
            AppointmentStatus cancelledStatus,
            Pageable pageable);

    List<AppointmentDetail> findUpcomingAppointments(
            LocalDateTime fromTime,
            AppointmentStatus excludedStatus,
            Pageable pageable);

    Page<AppointmentDetail> findStaffAppointments(
            String staffId,
            LocalDateTime startAt,
            LocalDateTime endAt,
            AppointmentStatus status,
            Pageable pageable);

    Page<AppointmentDetail> searchStaffAppointments(
            String staffId,
            String keyword,
            LocalDateTime startAt,
            LocalDateTime endAt,
            List<AppointmentStatus> statuses,
            Pageable pageable);

    Optional<AppointmentDetail> findByAppointmentIdAndStaffId(
            String appointmentId,
            String staffId);

    List<AppointmentDetail> findStaffAppointmentsForSchedule(
            String staffId,
            LocalDateTime startAt,
            LocalDateTime endAt,
            List<AppointmentStatus> statuses);

    List<Object[]> countStaffAppointmentsByStatus(
            String staffId,
            LocalDateTime startAt,
            LocalDateTime endAt);

    void save(AppointmentDetail appointmentDetail);
}
