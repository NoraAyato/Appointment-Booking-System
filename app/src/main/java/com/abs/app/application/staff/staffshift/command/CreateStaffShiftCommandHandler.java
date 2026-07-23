package com.abs.app.application.staff.staffshift.command;

import com.abs.app.common.constant.StaffShiftConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.repository.BlockedSlotRepository;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.domain.repository.StaffShiftRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.StaffAuthorizationService;
import com.abs.app.domain.service.StaffShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
    public class CreateStaffShiftCommandHandler {
    private final StaffShiftRepository staffShiftRepository;
    private final UserRepository userRepository;
    private final StaffShiftService staffShiftService;
    private final StaffAuthorizationService staffAuthorizationService;
    private final StaffServiceRepository staffServiceRepository;
    private final BlockedSlotRepository blockedSlotRepository;

    public void handle(CreateStaffShiftCommand command) {
        User staff = userRepository.findById(command.getUserId())
                        .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));
        staffAuthorizationService.ensureStaff(staff);

        if (!staffServiceRepository.existsByStaffUserIdAndStatus(command.getUserId(), StaffServiceStatus.ACTIVE))
        {
            throw new BusinessException(StaffShiftConstant.STAFF_NOT_ASSIGNED_SERVICE);
        }

        boolean isAllDayBlocked = blockedSlotRepository.existsAllDayOnDateByStatus(
                staff.getUserId(),
                command.getWorkDate(),
                BlockedSlotStatus.APPROVED);

        staffShiftService.customValidateStaffShiftTime(
                staff,
                command.getWorkDate(),
                command.getStartTime(),
                command.getEndTime(),
                null,
                isAllDayBlocked
        );

        StaffShift staffShift = new StaffShift();
        staffShift.setWorkDate(command.getWorkDate());
        staffShift.setStartTime(command.getStartTime());
        staffShift.setEndTime(command.getEndTime());
        staffShift.setStaff(staff);

        staffShiftRepository.save(staffShift);
    }
}
