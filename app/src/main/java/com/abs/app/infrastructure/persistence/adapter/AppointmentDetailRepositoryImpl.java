package com.abs.app.infrastructure.persistence.adapter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.repository.AppointmentDetailRepository;
import com.abs.app.infrastructure.persistence.jpa.AppointmentDetailJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentDetailRepositoryImpl implements AppointmentDetailRepository {

    private final AppointmentDetailJpaRepository appointmentDetailJpaRepository;

    @Override
    public Map<String, Integer> countCompletedServicesByStaffIds(
            List<String> staffIds,
            AppointmentStatus appointmentStatus) {
        if (staffIds == null || staffIds.isEmpty()) {
            return Map.of();
        }

        Map<String, Integer> completedServices = new HashMap<>();
        appointmentDetailJpaRepository.countCompletedServicesByStaffIds(staffIds, appointmentStatus)
                .forEach(row -> completedServices.put((String) row[0], ((Number) row[1]).intValue()));
        return completedServices;
    }

    @Override
    public List<Object[]> countDailyAppointments(
            LocalDateTime startAt,
            LocalDateTime endAt,
            AppointmentStatus completedStatus,
            AppointmentStatus cancelledStatus) {
        return appointmentDetailJpaRepository.countDailyAppointments(startAt, endAt, completedStatus, cancelledStatus);
    }

    @Override
    public List<Object[]> findTopServices(
            LocalDateTime startAt,
            LocalDateTime endAt,
            Pageable pageable) {
        return appointmentDetailJpaRepository.findTopServices(startAt, endAt, pageable);
    }

    @Override
    public List<Object[]> findStaffPerformance(
            LocalDateTime startAt,
            LocalDateTime endAt,
            LocalDate fromDate,
            LocalDate toDate,
            AppointmentStatus completedStatus,
            AppointmentStatus cancelledStatus,
            Pageable pageable) {
        return appointmentDetailJpaRepository.findStaffPerformance(
                startAt,
                endAt,
                fromDate,
                toDate,
                completedStatus,
                cancelledStatus,
                pageable);
    }

    @Override
    public List<AppointmentDetail> findUpcomingAppointments(
            LocalDateTime fromTime,
            AppointmentStatus excludedStatus,
            Pageable pageable) {
        return appointmentDetailJpaRepository.findUpcomingAppointments(fromTime, excludedStatus, pageable);
    }

    @Override
    public Page<AppointmentDetail> findStaffAppointments(
            String staffId,
            LocalDateTime startAt,
            LocalDateTime endAt,
            AppointmentStatus status,
            Pageable pageable) {
        return appointmentDetailJpaRepository.findStaffAppointments(staffId, startAt, endAt, status, pageable);
    }

    @Override
    public Page<AppointmentDetail> searchStaffAppointments(
            String staffId,
            String keyword,
            LocalDateTime startAt,
            LocalDateTime endAt,
            List<AppointmentStatus> statuses,
            Pageable pageable) {
        return appointmentDetailJpaRepository.searchStaffAppointments(
                staffId,
                keyword,
                startAt,
                endAt,
                statuses,
                pageable);
    }

    @Override
    public Page<AppointmentDetail> findBookingHistoryByCustomerId(
            String customerId,
            Pageable pageable) {
        return appointmentDetailJpaRepository.findBookingHistoryByCustomerId(customerId, pageable);
    }

    @Override
    public Optional<AppointmentDetail> findByAppointmentIdAndStaffId(
            String appointmentId,
            String staffId) {
        return appointmentDetailJpaRepository.findByAppointmentIdAndStaffId(appointmentId, staffId)
                .stream()
                .findFirst();
    }

    @Override
    public List<AppointmentDetail> findStaffAppointmentsForSchedule(
            String staffId,
            LocalDateTime startAt,
            LocalDateTime endAt,
            List<AppointmentStatus> statuses) {
        return appointmentDetailJpaRepository.findStaffAppointmentsForSchedule(
                staffId,
                startAt,
                endAt,
                statuses);
    }

    @Override
    public List<Object[]> countStaffAppointmentsByStatus(
            String staffId,
            LocalDateTime startAt,
            LocalDateTime endAt) {
        return appointmentDetailJpaRepository.countStaffAppointmentsByStatus(staffId, startAt, endAt);
    }

    @Override
    public void save(AppointmentDetail appointmentDetail) {
        appointmentDetailJpaRepository.save(appointmentDetail);
    }
}
