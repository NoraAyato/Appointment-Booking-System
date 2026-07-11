package com.abs.app.infrastructure.persistence.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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
}
