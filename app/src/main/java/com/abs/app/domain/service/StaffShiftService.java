package com.abs.app.domain.service;

import com.abs.app.common.constant.Messages;
import com.abs.app.common.constant.StaffShiftConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StaffShiftService {
    private final DateTimeService dateTimeService;

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
        if (blockedSlots == null || blockedSlots.isEmpty())
            return false;

        for (BlockedSlot slot : blockedSlots) {
            if (workDate.equals(slot.getBlockedDate()) &&
                    dateTimeService.isOverlapping(startTime, endTime, slot.getStartTime(), slot.getEndTime())) {
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
                // Bỏ qua chính ca làm việc đang được edit
                .filter(shift -> excludeShiftId == null || !Objects.equals(shift.getId(), excludeShiftId))
                // Kiểm tra trùng ngày và giao nhau về thời gian
                .anyMatch(shift -> shift.getWorkDate().equals(workDate) &&
                        shift.getStartTime().isBefore(endTime) &&
                        startTime.isBefore(shift.getEndTime()));
    }

    public void customValidateStaffShiftTime(User staff, LocalDate workDate, LocalTime startTime, LocalTime endTime,
            Long excludeShiftId) {
        // Kiểm tra logic thời gian Start < End
        if (!dateTimeService.isValidTimeRange(startTime, endTime)) {
            throw new BusinessException(Messages.INVALID_TIME);
        }

        // Kiểm tra ngày làm việc không được ở quá khứ
        if (!dateTimeService.isValidDate(workDate)) {
            throw new BusinessException(Messages.INVALID_DATE);
        }
        var blockedSlotsApproved = staff.getBlockedSlots().stream()
                .filter(slot -> slot.getStatus().name().equals(StaffShiftStatus.APPROVED.name()))
                .toList();
        // Kiểm tra xem nhân viên có đang xin nghỉ phép vào thời gian này không
        if (isValidWorkDateOverlapWithBlockedSlot(workDate, startTime, endTime, blockedSlotsApproved)) {
            throw new BusinessException(StaffShiftConstant.WORK_TIME_BLOCKED);
        }

        // Kiểm tra xem có bị trùng với các ca làm việc khác không
        if (isOverlapWorkDate(staff.getStaffShifts(), workDate, startTime, endTime, excludeShiftId)) {
            throw new BusinessException(StaffShiftConstant.WORK_TIME_OVERLAP);
        }
    }
}
