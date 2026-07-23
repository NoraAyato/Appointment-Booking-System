package com.abs.app.application.admin.staffshift.command;

import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.UserStatus;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.StaffShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service("adminCreateBulkStaffShiftCommandHandler")
@RequiredArgsConstructor
public class CreateBulkStaffShiftCommandHandler {
    private final UserRepository userRepository;
    private final StaffShiftService staffShiftService;
    private final StaffServiceRepository staffServiceRepository;

    @Transactional
    public void handle(CreateBulkStaffShiftCommand command) {
        List<User> allStaffs = userRepository.findAllByRoleRoleNameAndStatus(RoleEnum.STAFF, UserStatus.ACTIVE);

        allStaffs.stream()
                .filter(staff -> staffServiceRepository.existsByStaffUserIdAndStatus(staff.getUserId(), StaffServiceStatus.ACTIVE))
                .forEach(staff -> {
                    try {
                        staffShiftService.generateWeeklyShiftsForSingleStaff(
                                staff,
                                command.getStartDate(),
                                command.getEndDate(),
                                command.getWorkingDays(),
                                command.getStartTime(),
                                command.getEndTime()
                        );
                    } catch (Exception e) {
                        log.error("Lỗi tạo ca cho staff {}: {}", staff.getUserId(), e.getMessage());
                    }
                });
    }
}
