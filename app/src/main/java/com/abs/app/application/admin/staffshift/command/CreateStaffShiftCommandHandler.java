package com.abs.app.application.admin.staffshift.command;

import com.abs.app.common.constant.Messages;
import com.abs.app.common.constant.StaffShiftConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.domain.repository.StaffShiftRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.DateTimeService;
import com.abs.app.domain.service.StaffShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("adminCreateStaffShift")
@RequiredArgsConstructor
public class CreateStaffShiftCommandHandler {
    private final StaffShiftRepository staffShiftRepository;
    private final UserRepository userRepository;
    private final DateTimeService dateTimeService;
    private final StaffShiftService staffShiftService;

    public void handle(CreateStaffShiftCommand command) {
        User staff = userRepository.findById(command.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));

        if (!staff.getRole().getRoleName().equals(RoleEnum.STAFF))
            throw new BusinessException(StaffShiftConstant.ONLY_STAFF_ALLOWED);

        if (staffShiftService.isValidWorkDateOverlapWithBlockedSlot(command.getWorkDate(), command.getStartTime(), command.getEndTime(), staff.getBlockedSlots())) {
            throw new BusinessException(StaffShiftConstant.WORK_TIME_BLOCKED);
        }

        if (staffShiftService.isOverlapWorkDate(
                staff.getStaffShifts(),
                command.getWorkDate(), command.getStartTime(), command.getEndTime(),
                null)) {
            throw new BusinessException(StaffShiftConstant.WORK_TIME_OVERLAP);
        }

        if (!dateTimeService.isValidTimeRange(command.getStartTime(), command.getEndTime())) {
            throw new BusinessException(Messages.INVALID_TIME);
        }
        if (!dateTimeService.isValidDate(command.getWorkDate())) {
            throw new BusinessException(Messages.INVALID_DATE);
        }

        StaffShift staffShift = new StaffShift();
        staffShift.setWorkDate(command.getWorkDate());
        staffShift.setStartTime(command.getStartTime());
        staffShift.setEndTime(command.getEndTime());
        staffShift.setStatus(StaffShiftStatus.APPROVED);
        staffShift.setStaff(staff);

        staffShiftRepository.save(staffShift);
    }
}
