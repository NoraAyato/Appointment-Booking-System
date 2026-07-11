package com.abs.app.domain.repository;

import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.entity.enums.UserStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StaffShiftRepository {
    List<StaffShift> findAll();
    Page<StaffShift> search(String keyword, StaffShiftStatus status, Pageable pageable);
    Page<StaffShift> searchByStaffId(String staffId, String keyword, StaffShiftStatus status, Pageable pageable);
    List<StaffShift> findApprovedShiftsForService(
            String serviceId,
            LocalDate date,
            ServiceStatus serviceStatus,
            UserStatus staffStatus,
            StaffServiceStatus staffServiceStatus,
            StaffShiftStatus staffShiftStatus);
    Optional<StaffShift> findById(Long id);
    void save(StaffShift staffShift);
    void deleteById(Long id);
}
