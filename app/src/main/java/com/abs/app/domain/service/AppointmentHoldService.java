package com.abs.app.domain.service;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AppointmentHoldService {
    Optional<AppointmentHold> findByToken(String holdToken);

    boolean hold(
            AppointmentHold appointmentHold,
            long expirationMinutes);

    boolean isSlotHeld(String serviceId, String staffId, LocalDateTime startAt);

    void invalidate(String holdToken);

    record AppointmentHold(
            String holdToken,
            String customerId,
            String serviceId,
            String staffId,
            LocalDateTime startAt,
            LocalDateTime endAt) {
    }
}
