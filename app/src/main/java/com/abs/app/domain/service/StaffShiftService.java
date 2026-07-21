package com.abs.app.domain.service;

import com.abs.app.common.constant.Messages;
import com.abs.app.common.constant.StaffShiftConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.repository.BlockedSlotRepository;
import com.abs.app.domain.repository.StaffShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StaffShiftService {
    private final DateTimeService dateTimeService;
    private final BlockedSlotRepository blockedSlotRepository;
    private final StaffShiftRepository staffShiftRepository;

    public StaffShiftStatus handleStaffShiftStatus(String status) {
        return switch (status) {
            case "PENDING" -> StaffShiftStatus.PENDING;
            case "APPROVED" -> StaffShiftStatus.APPROVED;
            case "REJECTED" -> StaffShiftStatus.REJECTED;
            default -> throw new BusinessException(StaffShiftConstant.INVALID_BLOCKED_SLOT_STATUS);
        };
    }

    public boolean isValidWorkDateOverlapWithBlockedSlot(LocalDate workDate, LocalTime startTime, LocalTime endTime,
            List<BlockedSlot> blockedSlots) {
        if (blockedSlots == null || blockedSlots.isEmpty()) {
            return false;
        }

        for (BlockedSlot slot : blockedSlots) {
            boolean dateScopesOverlap = slot.getBlockedDate() == null || workDate.equals(slot.getBlockedDate());
            boolean timeScopesOverlap = isAllDayBlockedSlot(slot)
                    || dateTimeService.isOverlapping(startTime, endTime, slot.getStartTime(), slot.getEndTime());

            if (dateScopesOverlap && timeScopesOverlap) {
                return true;
            }
        }
        return false;
    }

    public boolean isOverlapWorkDate(List<StaffShift> existingShifts,
            LocalDate workDate,
            LocalTime startTime,
            LocalTime endTime,
            Long excludeShiftId) {
        if (existingShifts == null || existingShifts.isEmpty()) {
            return false;
        }

        return existingShifts.stream()
                .filter(shift -> excludeShiftId == null || !Objects.equals(shift.getId(), excludeShiftId))
                .anyMatch(shift -> shift.getWorkDate().equals(workDate) &&
                        shift.getStartTime().isBefore(endTime) &&
                        startTime.isBefore(shift.getEndTime()));
    }

    public void customValidateStaffShiftTime(User staff, LocalDate workDate, LocalTime startTime, LocalTime endTime,
            Long excludeShiftId) {
        if (!dateTimeService.isValidTimeRange(startTime, endTime)) {
            throw new BusinessException(Messages.INVALID_TIME);
        }

        if (!dateTimeService.isValidDate(workDate)) {
            throw new BusinessException(Messages.INVALID_DATE);
        }

        if (blockedSlotRepository.existsAllDayOnDateByStatus(
                staff.getUserId(),
                workDate,
                BlockedSlotStatus.APPROVED)) {
            throw new BusinessException(StaffShiftConstant.WORK_TIME_BLOCKED);
        }

        if (isOverlapWorkDate(staff.getStaffShifts(), workDate, startTime, endTime, excludeShiftId)) {
            throw new BusinessException(StaffShiftConstant.WORK_TIME_OVERLAP);
        }
    }

    private boolean isAllDayBlockedSlot(BlockedSlot slot) {
        return slot.getStartTime() == null || slot.getEndTime() == null;
    }

    @Transactional
    public void generateWeeklyShiftsForSingleStaff(
            User staff,
            LocalDate startDate,
            LocalDate endDate,
            List<Integer> workingDays,
            LocalTime startTime,
            LocalTime endTime) {

        List<StaffShift> shiftsToSave = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (dateTimeService.isValidDateRange(currentDate, endDate)) {
            int currentDayOfWeek = currentDate.getDayOfWeek().getValue();
            if (workingDays.contains(currentDayOfWeek)) {
                this.customValidateStaffShiftTime(staff, currentDate, startTime, endTime, null);

                StaffShift staffShift = new StaffShift();
                staffShift.setWorkDate(currentDate);
                staffShift.setStartTime(startTime);
                staffShift.setEndTime(endTime);
                staffShift.setStaff(staff);
                staffShift.setStatus(StaffShiftStatus.APPROVED);

                shiftsToSave.add(staffShift);
            }
            currentDate = currentDate.plusDays(1);
        }
        if (!shiftsToSave.isEmpty()) {
            staffShiftRepository.saveAll(shiftsToSave);
        }
    }
}
