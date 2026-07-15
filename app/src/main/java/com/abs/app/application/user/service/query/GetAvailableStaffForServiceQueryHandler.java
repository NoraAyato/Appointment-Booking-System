package com.abs.app.application.user.service.query;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.user.service.dto.StaffServiceResponseDto;
import com.abs.app.common.constant.ServiceEntityConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.entity.enums.UserStatus;
import com.abs.app.domain.repository.AppointmentDetailRepository;
import com.abs.app.domain.repository.ReviewsRepository;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.domain.service.AppointmentHoldService;
import com.abs.app.infrastructure.mapper.StaffServiceMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetAvailableStaffForServiceQueryHandler {

    private final ServiceRepository serviceRepository;
    private final StaffServiceRepository staffServiceRepository;
    private final ReviewsRepository reviewsRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final AppointmentHoldService appointmentHoldService;

    @Transactional(readOnly = true)
    public List<StaffServiceResponseDto> handle(GetAvailableStaffForServiceQuery query) {
        ServiceEntity service = serviceRepository.findById(query.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException(ServiceEntityConstant.NOT_EXIST));

        if (service.getStatus() != ServiceStatus.ACTIVE) {
            return List.of();
        }

        LocalDateTime requestedStartAt = query.getDate().atTime(query.getTime());
        LocalDateTime requestedEndAt = requestedStartAt.plusMinutes(service.getDurationMinutes());
        LocalTime requestedEndTime = requestedEndAt.toLocalTime();

        List<StaffService> availableStaffServices = staffServiceRepository.findAvailableStaffForService(
                query.getServiceId(),
                query.getDate(),
                query.getTime(),
                requestedEndTime,
                requestedStartAt,
                requestedEndAt,
                ServiceStatus.ACTIVE,
                UserStatus.ACTIVE,
                StaffServiceStatus.ACTIVE,
                StaffShiftStatus.APPROVED,
                BlockedSlotStatus.APPROVED,
                AppointmentStatus.CANCELLED);

        availableStaffServices = availableStaffServices.stream()
                .filter(staffService -> !appointmentHoldService.isSlotHeld(
                        query.getServiceId(),
                        staffService.getStaff().getUserId(),
                        requestedStartAt))
                .toList();

        if (availableStaffServices.isEmpty()) {
            return List.of();
        }

        List<String> staffIds = availableStaffServices.stream()
                .map(staffService -> staffService.getStaff().getUserId())
                .toList();

        Map<String, Double> ratingsByStaffId = reviewsRepository.findAverageRatingsByStaffIds(
                staffIds,
                ReviewsStatus.APPROVED);
        Map<String, Integer> completedServicesByStaffId = appointmentDetailRepository.countCompletedServicesByStaffIds(
                staffIds,
                AppointmentStatus.COMPLETED);
        Map<String, List<String>> specialtiesByStaffId = staffServiceRepository.findSpecialtiesByStaffIds(
                staffIds,
                StaffServiceStatus.ACTIVE,
                ServiceStatus.ACTIVE);

        return availableStaffServices.stream()
                .map(staffService -> StaffServiceMapper.toUserStaffServiceResponse(
                        staffService,
                        ratingsByStaffId,
                        completedServicesByStaffId,
                        specialtiesByStaffId))
                .toList();
    }
}
