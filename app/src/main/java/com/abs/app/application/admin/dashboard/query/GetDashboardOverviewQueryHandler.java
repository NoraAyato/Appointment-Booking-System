package com.abs.app.application.admin.dashboard.query;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.admin.dashboard.dto.DashboardOverviewResponseDto;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.InvoiceStatus;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.UserStatus;
import com.abs.app.domain.repository.AppointmentRepository;
import com.abs.app.domain.repository.InvoiceRepository;
import com.abs.app.domain.repository.ReviewsRepository;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetDashboardOverviewQueryHandler {
    private final AppointmentRepository appointmentRepository;
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final ReviewsRepository reviewsRepository;

    @Transactional(readOnly = true)
    public DashboardOverviewResponseDto handle(DashboardDateRangeQuery query) {
        DashboardDateRange range = DashboardDateRange.of(query.getFromDate(), query.getToDate());
        Map<AppointmentStatus, Long> appointmentStatusCounts = appointmentRepository.countAppointmentsByStatus(
                range.getStartAt(),
                range.getEndAtExclusive());

        return new DashboardOverviewResponseDto(
                appointmentRepository.countAppointmentsBetween(range.getStartAt(), range.getEndAtExclusive()),
                appointmentStatusCounts.getOrDefault(AppointmentStatus.PENDING, 0L),
                appointmentStatusCounts.getOrDefault(AppointmentStatus.CONFIRMED, 0L),
                appointmentStatusCounts.getOrDefault(AppointmentStatus.COMPLETED, 0L),
                appointmentStatusCounts.getOrDefault(AppointmentStatus.CANCELLED, 0L),
                invoiceRepository.sumAmountByStatusAndCreatedAtBetween(
                        InvoiceStatus.PAID,
                        range.getStartAt(),
                        range.getEndAtExclusive()),
                invoiceRepository.countByStatusAndCreatedAtBetween(
                        InvoiceStatus.PAID,
                        range.getStartAt(),
                        range.getEndAtExclusive()),
                userRepository.countByRoleAndStatusAndCreatedAtBetween(
                        RoleEnum.CUSTOMER,
                        UserStatus.ACTIVE,
                        range.getStartAt(),
                        range.getEndAtExclusive()),
                serviceRepository.countByStatusValue(ServiceStatus.ACTIVE),
                userRepository.countByRoleAndStatus(RoleEnum.STAFF, UserStatus.ACTIVE),
                reviewsRepository.countByStatus(ReviewsStatus.PENDING),
                reviewsRepository.findAverageRatingByStatus(ReviewsStatus.APPROVED));
    }
}
