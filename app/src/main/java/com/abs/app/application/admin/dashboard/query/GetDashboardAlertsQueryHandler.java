package com.abs.app.application.admin.dashboard.query;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.admin.dashboard.dto.DashboardAlertsResponseDto;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.repository.BlockedSlotRepository;
import com.abs.app.domain.repository.ReviewsRepository;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.repository.StaffShiftRepository;
import com.abs.app.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetDashboardAlertsQueryHandler {
    private final StaffShiftRepository staffShiftRepository;
    private final BlockedSlotRepository blockedSlotRepository;
    private final ReviewsRepository reviewsRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public DashboardAlertsResponseDto handle() {
        return new DashboardAlertsResponseDto(
                staffShiftRepository.countByStatus(StaffShiftStatus.PENDING),
                blockedSlotRepository.countByStatus(BlockedSlotStatus.PENDING),
                reviewsRepository.countByStatus(ReviewsStatus.PENDING),
                serviceRepository.countActiveServicesWithoutActiveStaff(),
                userRepository.countActiveStaffWithoutApprovedShiftOnDate(LocalDate.now()));
    }
}
