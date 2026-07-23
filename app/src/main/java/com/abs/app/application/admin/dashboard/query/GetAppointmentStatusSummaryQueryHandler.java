package com.abs.app.application.admin.dashboard.query;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.admin.dashboard.dto.DashboardStatusCountResponseDto;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetAppointmentStatusSummaryQueryHandler {
    private final AppointmentRepository appointmentRepository;

    @Transactional(readOnly = true)
    public List<DashboardStatusCountResponseDto> handle(DashboardDateRangeQuery query) {
        DashboardDateRange range = DashboardDateRange.of(query.getFromDate(), query.getToDate());
        Map<AppointmentStatus, Long> counts = appointmentRepository.countAppointmentsByStatus(
                range.getStartAt(),
                range.getEndAtExclusive());

        return Arrays.stream(AppointmentStatus.values())
                .map(status -> new DashboardStatusCountResponseDto(status.name(), counts.getOrDefault(status, 0L)))
                .toList();
    }
}
