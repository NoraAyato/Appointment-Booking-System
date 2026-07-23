package com.abs.app.application.admin.dashboard.query;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.admin.dashboard.dto.DashboardUpcomingAppointmentResponseDto;
import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.repository.AppointmentDetailRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUpcomingAppointmentsQueryHandler {
    private final AppointmentDetailRepository appointmentDetailRepository;

    @Transactional(readOnly = true)
    public List<DashboardUpcomingAppointmentResponseDto> handle(int limit) {
        return appointmentDetailRepository.findUpcomingAppointments(
                LocalDateTime.now(),
                AppointmentStatus.CANCELLED,
                PageRequest.of(0, normalizeLimit(limit)))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private DashboardUpcomingAppointmentResponseDto toResponse(AppointmentDetail appointmentDetail) {
        return new DashboardUpcomingAppointmentResponseDto(
                appointmentDetail.getAppointment().getId(),
                fullName(appointmentDetail.getAppointment().getCustomer()),
                appointmentDetail.getService() != null ? appointmentDetail.getService().getName() : "",
                fullName(appointmentDetail.getStaff()),
                appointmentDetail.getStartTime(),
                appointmentDetail.getEndTime(),
                appointmentDetail.getAppointment().getStatus().name());
    }

    private String fullName(User user) {
        if (user == null) {
            return "";
        }

        String name = ((user.getFirstName() != null ? user.getFirstName() : "") + " "
                + (user.getLastName() != null ? user.getLastName() : "")).trim();
        return !name.isBlank() ? name : user.getUserName();
    }

    private int normalizeLimit(int limit) {
        if (limit < 1) {
            return 10;
        }
        return Math.min(limit, 100);
    }
}
