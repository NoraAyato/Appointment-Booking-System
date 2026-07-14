package com.abs.app.application.staff.dashboard.query;

import com.abs.app.application.staff.dashboard.dto.StaffDashboardAppointmentResponseDto;
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
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.repository.AppointmentDetailRepository;
import com.abs.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetStaffDashboardAppointmentsQueryHandler {
    private final UserRepository userRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;

    @Transactional(readOnly = true)
    public PageResponse<StaffDashboardAppointmentResponseDto> handle(GetStaffDashboardAppointmentsQuery query) {
        ensureStaff(query.getStaffId());

        Optional<AppointmentStatus> status = EnumUtil.parse(AppointmentStatus.class, query.getStatus());
        if (EnumUtil.isInvalidEnumValue(query.getStatus(), status)) {
            return PaginationUtil.emptyResponse(query.getPage(), query.getLimit());
        }

        StaffDashboardDateRange dateRange = StaffDashboardDateRange.of(query.getFromDate(), query.getToDate());
        Pageable pageable = PaginationUtil.createPageable(
                query.getPage(),
                query.getLimit(),
                Sort.by("startTime").ascending());

        Page<AppointmentDetail> appointments = appointmentDetailRepository.findStaffAppointments(
                query.getStaffId(),
                dateRange.startAt(),
                dateRange.endExclusive(),
                status.orElse(null),
                pageable);

        return PaginationUtil.toPageResponse(
                appointments,
                StaffDashboardMapper::toAppointmentResponse,
                query.getPage(),
                query.getLimit());
    }

    private void ensureStaff(String staffId) {
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));

        if (staff.getRole() == null || !RoleEnum.STAFF.equals(staff.getRole().getRoleName())) {
            throw new BusinessException(StaffShiftConstant.ONLY_STAFF_ALLOWED);
        }
    }
}
