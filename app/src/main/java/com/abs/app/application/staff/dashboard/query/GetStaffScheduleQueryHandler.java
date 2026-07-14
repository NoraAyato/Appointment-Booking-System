package com.abs.app.application.staff.dashboard.query;

import com.abs.app.application.staff.dashboard.dto.StaffScheduleEventResponseDto;
import com.abs.app.common.constant.StaffShiftConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.BlockedSlot;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetStaffScheduleQueryHandler {
    private final UserRepository userRepository;
    private final StaffShiftRepository staffShiftRepository;
    private final BlockedSlotRepository blockedSlotRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;

    @Transactional(readOnly = true)
    public List<StaffScheduleEventResponseDto> handle(GetStaffScheduleQuery query) {
        ensureStaff(query.getStaffId());

        StaffDashboardDateRange dateRange = StaffDashboardDateRange.of(query.getFromDate(), query.getToDate());
        List<StaffScheduleEventResponseDto> events = new ArrayList<>();

        staffShiftRepository.findApprovedByStaffIdAndWorkDateBetween(
                query.getStaffId(),
                dateRange.getFromDate(),
                dateRange.getToDate())
                .stream()
                .map(StaffDashboardMapper::toShiftEvent)
                .forEach(events::add);

        List<AppointmentDetail> appointments = appointmentDetailRepository.findStaffAppointmentsForSchedule(
                query.getStaffId(),
                dateRange.startAt(),
                dateRange.endExclusive(),
                AppointmentStatus.CANCELLED);
        appointments.stream()
                .map(StaffDashboardMapper::toAppointmentEvent)
                .forEach(events::add);

        blockedSlotRepository.findApprovedVisibleToStaffInDateRange(
                query.getStaffId(),
                dateRange.getFromDate(),
                dateRange.getToDate())
                .forEach(blockedSlot -> addBlockedSlotEvents(events, blockedSlot, dateRange));

        events.sort(Comparator
                .comparing(StaffScheduleEventResponseDto::getDate)
                .thenComparing(event -> dateRange.eventStartTimeOrMin(event.getStartTime()))
                .thenComparing(StaffScheduleEventResponseDto::getType));

        return events;
    }

    private void addBlockedSlotEvents(
            List<StaffScheduleEventResponseDto> events,
            BlockedSlot blockedSlot,
            StaffDashboardDateRange dateRange) {
        if (blockedSlot.getBlockedDate() != null) {
            events.add(StaffDashboardMapper.toBlockedSlotEvent(blockedSlot, blockedSlot.getBlockedDate()));
            return;
        }

        LocalDate currentDate = dateRange.getFromDate();
        while (!currentDate.isAfter(dateRange.getToDate())) {
            events.add(StaffDashboardMapper.toBlockedSlotEvent(blockedSlot, currentDate));
            currentDate = currentDate.plusDays(1);
        }
    }

    private void ensureStaff(String staffId) {
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));

        if (staff.getRole() == null || !RoleEnum.STAFF.equals(staff.getRole().getRoleName())) {
            throw new BusinessException(StaffShiftConstant.ONLY_STAFF_ALLOWED);
        }
    }
}
