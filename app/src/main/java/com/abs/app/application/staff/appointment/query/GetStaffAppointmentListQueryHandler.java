package com.abs.app.application.staff.appointment.query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.staff.appointment.dto.StaffAppointmentResponseDto;
import com.abs.app.common.constant.StaffShiftConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.EnumUtil;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.repository.AppointmentDetailRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.StaffAuthorizationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetStaffAppointmentListQueryHandler {
    private final UserRepository userRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final StaffAuthorizationService staffAuthorizationService;

    @Transactional(readOnly = true)
    public PageResponse<StaffAppointmentResponseDto> handle(GetStaffAppointmentListQuery query) {
        User staff = userRepository.findById(query.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));
        staffAuthorizationService.ensureStaff(staff);

        Optional<AppointmentStatus> status = EnumUtil.parse(AppointmentStatus.class, query.getStatus());
        if (EnumUtil.isInvalidEnumValue(query.getStatus(), status)) {
            return PaginationUtil.emptyResponse(query.getPage(), query.getLimit());
        }

        List<AppointmentStatus> statuses = getAllowedStatuses(status);
        if (statuses.isEmpty()) {
            return PaginationUtil.emptyResponse(query.getPage(), query.getLimit());
        }

        LocalDateTime startAt = query.getDate() != null ? query.getDate().atStartOfDay() : null;
        LocalDateTime endAt = query.getDate() != null ? query.getDate().plusDays(1).atStartOfDay() : null;
        Pageable pageable = PaginationUtil.createPageable(
                query.getPage(),
                query.getLimit(),
                Sort.by("startTime").ascending());

        Page<AppointmentDetail> appointments = appointmentDetailRepository.searchStaffAppointments(
                query.getStaffId(),
                query.getKeyword(),
                startAt,
                endAt,
                statuses,
                pageable);

        return PaginationUtil.toPageResponse(
                appointments,
                StaffAppointmentMapper::toResponse,
                query.getPage(),
                query.getLimit());
    }

    private List<AppointmentStatus> getAllowedStatuses(Optional<AppointmentStatus> status) {
        if (status.isEmpty()) {
            return List.of(AppointmentStatus.CONFIRMED, AppointmentStatus.COMPLETED);
        }

        AppointmentStatus requestedStatus = status.get();
        if (AppointmentStatus.CONFIRMED.equals(requestedStatus)
                || AppointmentStatus.COMPLETED.equals(requestedStatus)) {
            return List.of(requestedStatus);
        }

        return List.of();
    }
}
