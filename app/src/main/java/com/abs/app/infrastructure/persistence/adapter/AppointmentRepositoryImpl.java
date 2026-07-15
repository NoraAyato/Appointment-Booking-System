package com.abs.app.infrastructure.persistence.adapter;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.app.domain.entity.Appointment;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.repository.AppointmentRepository;
import com.abs.app.infrastructure.persistence.jpa.AppointmentJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentRepositoryImpl implements AppointmentRepository {
    private final AppointmentJpaRepository appointmentJpaRepository;

    @Override
    public Appointment save(Appointment appointment) {
        return appointmentJpaRepository.save(appointment);
    }

    @Override
    public long countAppointmentsBetween(LocalDateTime startAt, LocalDateTime endAt) {
        return appointmentJpaRepository.countAppointmentsBetween(startAt, endAt);
    }

    @Override
    public Map<AppointmentStatus, Long> countAppointmentsByStatus(LocalDateTime startAt, LocalDateTime endAt) {
        Map<AppointmentStatus, Long> totals = new EnumMap<>(AppointmentStatus.class);
        appointmentJpaRepository.countAppointmentsByStatus(startAt, endAt)
                .forEach(row -> totals.put((AppointmentStatus) row[0], ((Number) row[1]).longValue()));
        return totals;
    }
}
