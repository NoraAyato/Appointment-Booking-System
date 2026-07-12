package com.abs.app.infrastructure.persistence.adapter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
