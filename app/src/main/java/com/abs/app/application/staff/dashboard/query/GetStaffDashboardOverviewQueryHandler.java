package com.abs.app.application.staff.dashboard.query;

import com.abs.app.application.staff.dashboard.dto.StaffDashboardOverviewResponseDto;
import com.abs.app.common.constant.StaffShiftConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.repository.AppointmentDetailRepository;
import com.abs.app.domain.repository.BlockedSlotRepository;
import com.abs.app.domain.repository.StaffShiftRepository;
import com.abs.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetStaffDashboardOverviewQueryHandler {
    private final UserRepository userRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final StaffShiftRepository staffShiftRepository;
    private final BlockedSlotRepository blockedSlotRepository;

    @Transactional(readOnly = true)
    public StaffDashboardOverviewResponseDto handle(GetStaffDashboardOverviewQuery query) {
        ensureStaff(query.getStaffId());

        StaffDashboardDateRange dateRange = StaffDashboardDateRange.of(query.getFromDate(), query.getToDate());
        Map<AppointmentStatus, Integer> countsByStatus = getCountsByStatus(query, dateRange);

        List<StaffShift> shifts = staffShiftRepository.findApprovedByStaffIdAndWorkDateBetween(
                query.getStaffId(),
                dateRange.getFromDate(),
                dateRange.getToDate());

        int blockedSlots = blockedSlotRepository.findApprovedByStaffIdInDateRange(
                query.getStaffId(),
                dateRange.getFromDate(),
                dateRange.getToDate()).size();

        StaffDashboardOverviewResponseDto dto = new StaffDashboardOverviewResponseDto();
        dto.setPendingAppointments(countsByStatus.getOrDefault(AppointmentStatus.PENDING, 0));
        dto.setConfirmedAppointments(countsByStatus.getOrDefault(AppointmentStatus.CONFIRMED, 0));
        dto.setCompletedAppointments(countsByStatus.getOrDefault(AppointmentStatus.COMPLETED, 0));
        dto.setCancelledAppointments(countsByStatus.getOrDefault(AppointmentStatus.CANCELLED, 0));
        dto.setTotalAppointments(countsByStatus.values().stream().mapToInt(Integer::intValue).sum());
        dto.setTotalWorkingHours(toHours(shifts));
        dto.setTotalBlockedSlots(blockedSlots);
        return dto;
    }

    private Map<AppointmentStatus, Integer> getCountsByStatus(
            GetStaffDashboardOverviewQuery query,
            StaffDashboardDateRange dateRange) {
        Map<AppointmentStatus, Integer> countsByStatus = new EnumMap<>(AppointmentStatus.class);
        appointmentDetailRepository.countStaffAppointmentsByStatus(
                query.getStaffId(),
                dateRange.startAt(),
                dateRange.endExclusive())
                .forEach(row -> countsByStatus.put(toAppointmentStatus(row[0]), ((Number) row[1]).intValue()));
        return countsByStatus;
    }

    private AppointmentStatus toAppointmentStatus(Object value) {
        if (value instanceof AppointmentStatus appointmentStatus) {
            return appointmentStatus;
        }
        return AppointmentStatus.valueOf(String.valueOf(value));
    }

    private double toHours(List<StaffShift> shifts) {
        long minutes = shifts.stream()
                .filter(shift -> shift.getStartTime() != null && shift.getEndTime() != null)
                .mapToLong(shift -> Duration.between(shift.getStartTime(), shift.getEndTime()).toMinutes())
                .sum();
        return Math.round((minutes / 60.0) * 100.0) / 100.0;
    }

    private void ensureStaff(String staffId) {
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));

        if (staff.getRole() == null || !RoleEnum.STAFF.equals(staff.getRole().getRoleName())) {
            throw new BusinessException(StaffShiftConstant.ONLY_STAFF_ALLOWED);
        }
    }
}
