package com.abs.app.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.abs.app.domain.entity.enums.AppointmentStatus;

public interface AppointmentRepository {
    long countAppointmentsBetween(LocalDateTime startAt, LocalDateTime endAt);

    Map<AppointmentStatus, Long> countAppointmentsByStatus(LocalDateTime startAt, LocalDateTime endAt);
}
