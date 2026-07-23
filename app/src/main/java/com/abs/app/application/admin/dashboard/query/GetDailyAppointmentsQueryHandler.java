package com.abs.app.application.admin.dashboard.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.admin.dashboard.dto.DashboardDailyAppointmentsResponseDto;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.repository.AppointmentDetailRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetDailyAppointmentsQueryHandler {
    private final AppointmentDetailRepository appointmentDetailRepository;

    @Transactional(readOnly = true)
    public List<DashboardDailyAppointmentsResponseDto> handle(DashboardDateRangeQuery query) {
        DashboardDateRange range = DashboardDateRange.of(query.getFromDate(), query.getToDate());

        return appointmentDetailRepository.countDailyAppointments(
                range.getStartAt(),
                range.getEndAtExclusive(),
                AppointmentStatus.COMPLETED,
                AppointmentStatus.CANCELLED)
                .stream()
                .map(row -> new DashboardDailyAppointmentsResponseDto(
                        DashboardRowMapper.localDateValue(row[0]),
                        DashboardRowMapper.longValue(row[1]),
                        DashboardRowMapper.longValue(row[2]),
                        DashboardRowMapper.longValue(row[3])))
                .toList();
    }
}
