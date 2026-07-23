package com.abs.app.application.admin.dashboard.query;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.admin.dashboard.dto.DashboardStaffPerformanceResponseDto;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.repository.AppointmentDetailRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetStaffPerformanceQueryHandler {
    private final AppointmentDetailRepository appointmentDetailRepository;

    @Transactional(readOnly = true)
    public List<DashboardStaffPerformanceResponseDto> handle(DashboardLimitQuery query) {
        DashboardDateRange range = DashboardDateRange.of(query.getFromDate(), query.getToDate());

        return appointmentDetailRepository.findStaffPerformance(
                range.getStartAt(),
                range.getEndAtExclusive(),
                range.getFromDate(),
                range.getToDate(),
                AppointmentStatus.COMPLETED,
                AppointmentStatus.CANCELLED,
                PageRequest.of(0, normalizeLimit(query.getLimit())))
                .stream()
                .map(row -> new DashboardStaffPerformanceResponseDto(
                        DashboardRowMapper.stringValue(row[0]),
                        DashboardRowMapper.stringValue(row[1]),
                        DashboardRowMapper.longValue(row[2]),
                        DashboardRowMapper.longValue(row[3]),
                        DashboardRowMapper.doubleValue(row[4]),
                        DashboardRowMapper.doubleValue(row[5])))
                .toList();
    }

    private int normalizeLimit(int limit) {
        if (limit < 1) {
            return 10;
        }
        return Math.min(limit, 100);
    }
}
