package com.abs.app.application.staff.staffshift.command;

import com.abs.app.common.constant.BlockedSlotConstant;
import com.abs.app.common.constant.Messages;
import com.abs.app.common.constant.StaffShiftConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.repository.StaffShiftRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.DateTimeService;
import com.abs.app.domain.service.StaffShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateStaffShiftCommandHandler {
    private final StaffShiftRepository staffShiftRepository;
    private final UserRepository userRepository;
    private final StaffShiftService staffShiftService;
    private final DateTimeService dateTimeService;

    public void handle(CreateStaffShiftCommand command) {
        User staff = userRepository.findById(command.getUserId())
                        .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));

        staffShiftService.customValidateStaffShiftTime(
                staff,
                command.getWorkDate(),
                command.getStartTime(),
                command.getEndTime(),
                null
        );

        StaffShift staffShift = new StaffShift();
        staffShift.setWorkDate(command.getWorkDate());
        staffShift.setStartTime(command.getStartTime());
        staffShift.setEndTime(command.getEndTime());
        staffShift.setStaff(staff);

        staffShiftRepository.save(staffShift);
    }
}
