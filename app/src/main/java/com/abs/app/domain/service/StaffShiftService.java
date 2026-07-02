package com.abs.app.domain.service;

import com.abs.app.common.constant.StaffShiftConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.StaffShift;
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

    public boolean isValidWorkDateOverlapWithBlockedSlot(LocalDate workDate, LocalTime startTime, LocalTime endTime, List<BlockedSlot> blockedSlots) {
        if (blockedSlots == null || blockedSlots.isEmpty()) return false;

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
}
