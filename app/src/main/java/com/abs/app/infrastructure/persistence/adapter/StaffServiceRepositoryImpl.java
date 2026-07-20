package com.abs.app.infrastructure.persistence.adapter;

import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.entity.enums.UserStatus;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.infrastructure.persistence.jpa.StaffServiceJpaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StaffServiceRepositoryImpl implements StaffServiceRepository {

    private final StaffServiceJpaRepository staffServiceJpaRepository;

    @Override
    public List<StaffService> findAll() {
        return staffServiceJpaRepository.findAll();
    }

    @Override
    public Page<StaffService> search(String keyword, StaffServiceStatus status, Pageable pageable) {
        return staffServiceJpaRepository.search(keyword, status, pageable);
    }

    @Override
    public Optional<StaffService> findById(Long id) {
        return staffServiceJpaRepository.findById(id);
    }

    @Override
    public Optional<StaffService> findByStaffUserIdAndServiceId(String staffId, String staffServiceId) {
        return staffServiceJpaRepository.findByStaffUserIdAndServiceId(staffId, staffServiceId);
    }

    @Override
    public boolean existsByStaffUserIdAndStatus(String staffId, StaffServiceStatus status) {
        return staffServiceJpaRepository.existsByStaffUserIdAndStatus(staffId, status);
    }

    @Override
    public List<StaffService> findAvailableStaffForService(
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
            AppointmentStatus excludedAppointmentStatus) {
        return staffServiceJpaRepository.findAvailableStaffForService(
                serviceId,
                date,
                time,
                requestedEndTime,
                requestedStartAt,
                requestedEndAt,
                serviceStatus,
                staffStatus,
                staffServiceStatus,
                staffShiftStatus,
                blockedSlotStatus,
                excludedAppointmentStatus);
    }

    @Override
    public long countAvailableStaffForService(
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
            AppointmentStatus excludedAppointmentStatus) {
        return staffServiceJpaRepository.countAvailableStaffForService(
                serviceId,
                date,
                time,
                requestedEndTime,
                requestedStartAt,
                requestedEndAt,
                serviceStatus,
                staffStatus,
                staffServiceStatus,
                staffShiftStatus,
                blockedSlotStatus,
                excludedAppointmentStatus);
    }

    @Override
    public Map<String, List<String>> findSpecialtiesByStaffIds(
            List<String> staffIds,
            StaffServiceStatus staffServiceStatus,
            ServiceStatus serviceStatus) {
        if (staffIds == null || staffIds.isEmpty()) {
            return Map.of();
        }

        Map<String, List<String>> specialties = new HashMap<>();
        staffServiceJpaRepository.findSpecialtiesByStaffIds(staffIds, staffServiceStatus, serviceStatus)
                .forEach(row -> specialties
                        .computeIfAbsent((String) row[0], key -> new ArrayList<>())
                        .add((String) row[1]));
        return specialties;
    }

    @Override
    public void save(StaffService staffService) {
        staffServiceJpaRepository.save(staffService);
    }

    @Override
    public void deleteById(Long id) {
        staffServiceJpaRepository.deleteById(id);
    }
}
