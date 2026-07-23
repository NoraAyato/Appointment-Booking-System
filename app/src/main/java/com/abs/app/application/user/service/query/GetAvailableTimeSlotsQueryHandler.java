package com.abs.app.application.user.service.query;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.user.service.dto.AvailableTimeSlotResponseDto;
import com.abs.app.common.constant.ServiceEntityConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.entity.enums.UserStatus;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.domain.repository.StaffShiftRepository;
import com.abs.app.domain.service.AppointmentHoldService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetAvailableTimeSlotsQueryHandler {

    private static final int SLOT_INTERVAL_MINUTES = 30;

    private final ServiceRepository serviceRepository;
    private final StaffShiftRepository staffShiftRepository;
    private final StaffServiceRepository staffServiceRepository;
    private final AppointmentHoldService appointmentHoldService;

    @Transactional(readOnly = true)
    public List<AvailableTimeSlotResponseDto> handle(GetAvailableTimeSlotsQuery query) {
        ServiceEntity service = serviceRepository.findById(query.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException(ServiceEntityConstant.NOT_EXIST));

        if (service.getStatus() != ServiceStatus.ACTIVE) {
            return List.of();
        }

        List<StaffShift> shifts = staffShiftRepository.findApprovedShiftsForService(
                query.getServiceId(),
                query.getDate(),
                ServiceStatus.ACTIVE,
                UserStatus.ACTIVE,
                StaffServiceStatus.ACTIVE,
                StaffShiftStatus.APPROVED);

        Set<LocalTime> candidateStartTimes = generateCandidateStartTimes(
                query,
                service.getDurationMinutes(),
                shifts);

        List<AvailableTimeSlotResponseDto> availableSlots = new ArrayList<>();
        for (LocalTime startTime : candidateStartTimes) {
            LocalDateTime requestedStartAt = query.getDate().atTime(startTime);
            LocalDateTime requestedEndAt = requestedStartAt.plusMinutes(service.getDurationMinutes());
            LocalTime requestedEndTime = requestedEndAt.toLocalTime();

            List<StaffService> availableStaffServices = staffServiceRepository.findAvailableStaffForService(
                    query.getServiceId(),
                    query.getDate(),
                    startTime,
                    requestedEndTime,
                    requestedStartAt,
                    requestedEndAt,
                    ServiceStatus.ACTIVE,
                    UserStatus.ACTIVE,
                    StaffServiceStatus.ACTIVE,
                    StaffShiftStatus.APPROVED,
                    BlockedSlotStatus.APPROVED,
                    AppointmentStatus.CANCELLED);

            long availableStaffCount = availableStaffServices.stream()
                    .filter(staffService -> !appointmentHoldService.isSlotHeld(
                            query.getServiceId(),
                            staffService.getStaff().getUserId(),
                            requestedStartAt))
                    .count();

            if (availableStaffCount > 0) {
                availableSlots.add(new AvailableTimeSlotResponseDto(
                        startTime,
                        requestedEndTime,
                        (int) availableStaffCount));
            }
        }

        return availableSlots.stream()
                .sorted(Comparator.comparing(AvailableTimeSlotResponseDto::getStartTime))
                .toList();
    }

    private Set<LocalTime> generateCandidateStartTimes(
            GetAvailableTimeSlotsQuery query,
            int durationMinutes,
            List<StaffShift> shifts) {
        Set<LocalTime> candidateStartTimes = new TreeSet<>();

        for (StaffShift shift : shifts) {
            LocalDateTime shiftStartAt = query.getDate().atTime(shift.getStartTime());
            LocalDateTime shiftEndAt = query.getDate().atTime(shift.getEndTime());

            if (!shiftEndAt.isAfter(shiftStartAt)) {
                continue;
            }

            LocalDateTime cursor = shiftStartAt;
            while (!cursor.plusMinutes(durationMinutes).isAfter(shiftEndAt)) {
                candidateStartTimes.add(cursor.toLocalTime());
                cursor = cursor.plusMinutes(SLOT_INTERVAL_MINUTES);
            }
        }

        return candidateStartTimes;
    }
}
