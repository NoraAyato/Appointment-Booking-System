package com.abs.app.domain.repository;

import java.util.List;
import java.util.Map;

import com.abs.app.domain.entity.enums.AppointmentStatus;

public interface AppointmentDetailRepository {
    Map<String, Integer> countCompletedServicesByStaffIds(
            List<String> staffIds,
            AppointmentStatus appointmentStatus);
}
