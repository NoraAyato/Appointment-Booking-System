package com.abs.app.application.admin.dashboard.query;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.admin.dashboard.dto.DashboardTopServiceResponseDto;
import com.abs.app.domain.repository.AppointmentDetailRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetTopServicesQueryHandler {
    private final AppointmentDetailRepository appointmentDetailRepository;

    @Transactional(readOnly = true)
    public List<DashboardTopServiceResponseDto> handle(DashboardLimitQuery query) {
        DashboardDateRange range = DashboardDateRange.of(query.getFromDate(), query.getToDate());

        return appointmentDetailRepository.findTopServices(
                range.getStartAt(),
                range.getEndAtExclusive(),
                PageRequest.of(0, normalizeLimit(query.getLimit())))
                .stream()
                .map(row -> new DashboardTopServiceResponseDto(
                        DashboardRowMapper.stringValue(row[0]),
                        DashboardRowMapper.stringValue(row[1]),
                        DashboardRowMapper.longValue(row[2]),
                        DashboardRowMapper.doubleValue(row[3]),
                        DashboardRowMapper.doubleValue(row[4])))
                .toList();
    }

    private int normalizeLimit(int limit) {
        if (limit < 1) {
            return 5;
        }
        return Math.min(limit, 100);
    }
}
