package com.abs.app.domain.repository;

import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.entity.enums.UserStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StaffServiceRepository {
    List<StaffService> findAll();
    Page<StaffService> search(String keyword, StaffServiceStatus status, Pageable pageable);
    Optional<StaffService> findById(Long id);
    Optional<StaffService> findByStaffUserIdAndServiceId(String staffId, String staffServiceId);
    List<StaffService> findAvailableStaffForService(
            String serviceId,
            LocalDate date,
            LocalTime time,
            LocalTime requestedEndTime,
            LocalDateTime requestedStartAt,
            LocalDateTime requestedEndAt,
            ServiceStatus serviceStatus,
            UserStatus staffStatus,
            StaffServiceStatus staffServiceStatus,
            StaffShiftStatus staffShiftStatus,
            BlockedSlotStatus blockedSlotStatus,
            AppointmentStatus excludedAppointmentStatus);
    long countAvailableStaffForService(
            String serviceId,
            LocalDate date,
            LocalTime time,
            LocalTime requestedEndTime,
            LocalDateTime requestedStartAt,
            LocalDateTime requestedEndAt,
            ServiceStatus serviceStatus,
            UserStatus staffStatus,
            StaffServiceStatus staffServiceStatus,
            StaffShiftStatus staffShiftStatus,
            BlockedSlotStatus blockedSlotStatus,
            AppointmentStatus excludedAppointmentStatus);
    Map<String, List<String>> findSpecialtiesByStaffIds(
            List<String> staffIds,
            StaffServiceStatus staffServiceStatus,
            ServiceStatus serviceStatus);
    void save(StaffService staffService);
    void deleteById(Long id);
}
