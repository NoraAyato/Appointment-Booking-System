package com.abs.app.application.staff.staffshift.command;

import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.repository.StaffShiftRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.DateTimeService;
import com.abs.app.domain.service.StaffAuthorizationService;
import com.abs.app.domain.service.StaffShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateBulkStaffShiftCommandHandler {
    private final StaffShiftRepository staffShiftRepository;
    private final UserRepository userRepository;
    private final StaffShiftService staffShiftService;
    private final StaffAuthorizationService staffAuthorizationService;
    private final DateTimeService dateTimeService;

    @Transactional
    public void handle(CreateBulkStaffShiftCommand command) {
        User staff = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));
        staffAuthorizationService.ensureStaff(staff);

        List<StaffShift> shiftsToSave = new ArrayList<>();
        LocalDate currentDate = command.getStartDate();

        while (dateTimeService.isValidDateRange(currentDate, command.getEndDate())) {
            int currentDayOfWeek = currentDate.getDayOfWeek().getValue();

            if (command.getWorkingDays().contains(currentDayOfWeek)) {
                staffShiftService.customValidateStaffShiftTime(
                        staff,
                        currentDate,
                        command.getStartTime(),
                        command.getEndTime(),
                        null
                );
                StaffShift staffShift = new StaffShift();
                staffShift.setWorkDate(currentDate);
                staffShift.setStartTime(command.getStartTime());
                staffShift.setEndTime(command.getEndTime());
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
