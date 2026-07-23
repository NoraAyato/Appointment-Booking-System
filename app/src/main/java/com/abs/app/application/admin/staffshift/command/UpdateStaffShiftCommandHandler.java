package com.abs.app.application.admin.staffshift.command;

import com.abs.app.common.constant.Messages;
import com.abs.app.common.constant.RoleConstant;
import com.abs.app.common.constant.StaffShiftConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.Role;
import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.repository.BlockedSlotRepository;
import com.abs.app.domain.repository.RoleRepository;
import com.abs.app.domain.repository.StaffShiftRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.DateTimeService;
import com.abs.app.domain.service.StaffShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("adminUpdateStaffShift")
@RequiredArgsConstructor
public class UpdateStaffShiftCommandHandler {
    private final StaffShiftRepository staffShiftRepository;
    private final UserRepository userRepository;
    private final DateTimeService dateTimeService;
    private final StaffShiftService staffShiftService;
    private final BlockedSlotRepository blockedSlotRepository;

    public void handle(UpdateStaffShiftCommand command) {
        StaffShift staffShiftEdit = staffShiftRepository.findById(command.getId())
                .orElseThrow(() -> new ResourceNotFoundException(StaffShiftConstant.STAFF_SHIFT_NOT_FOUND));

        User staff = userRepository.findById(command.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));

        boolean isAllDayBlocked = blockedSlotRepository.existsAllDayOnDateByStatus(
                staff.getUserId(),
                command.getWorkDate(),
                BlockedSlotStatus.APPROVED);

        staffShiftService.customValidateStaffShiftTime(
                staff,
                command.getWorkDate(),
                command.getStartTime(),
                command.getEndTime(),
                staffShiftEdit.getId(),
                isAllDayBlocked
        );

        StaffShiftStatus staffShiftStatus = staffShiftService.handleStaffShiftStatus(command.getStatus());
        staffShiftEdit.setWorkDate(command.getWorkDate());
        staffShiftEdit.setStartTime(command.getStartTime());
        staffShiftEdit.setEndTime(command.getEndTime());
        staffShiftEdit.setStaff(staff);
        staffShiftEdit.setStatus(staffShiftStatus);

        staffShiftRepository.save(staffShiftEdit);
    }
}
