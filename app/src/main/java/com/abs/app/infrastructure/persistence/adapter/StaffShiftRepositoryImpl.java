package com.abs.app.infrastructure.persistence.adapter;

import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.entity.enums.UserStatus;
import com.abs.app.domain.repository.StaffShiftRepository;
import com.abs.app.infrastructure.persistence.jpa.StaffShiftJpaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StaffShiftRepositoryImpl implements StaffShiftRepository {

    private final StaffShiftJpaRepository staffShiftJpaRepository;

    @Override
    public List<StaffShift> findAll() {
        return staffShiftJpaRepository.findAll();
    }

    @Override
    public Page<StaffShift> search(String keyword, StaffShiftStatus status, Pageable pageable) {
        return staffShiftJpaRepository.search(keyword, status, pageable);
    }

    @Override
    public Page<StaffShift> searchByStaffId(String staffId, String keyword, StaffShiftStatus status, Pageable pageable) {
        return staffShiftJpaRepository.searchByStaffId(staffId, keyword, status, pageable);
    }

    @Override
    public List<StaffShift> findApprovedShiftsForService(
            String serviceId,
            LocalDate date,
            ServiceStatus serviceStatus,
            UserStatus staffStatus,
            StaffServiceStatus staffServiceStatus,
            StaffShiftStatus staffShiftStatus) {
        return staffShiftJpaRepository.findApprovedShiftsForService(
                serviceId,
                date,
                serviceStatus,
                staffStatus,
                staffServiceStatus,
                staffShiftStatus);
    }

    @Override
    public List<StaffShift> findApprovedByStaffIdAndWorkDateBetween(
            String staffId,
            LocalDate fromDate,
            LocalDate toDate) {
        return staffShiftJpaRepository.findByStaffIdAndStatusAndWorkDateBetween(
                staffId,
                StaffShiftStatus.APPROVED,
                fromDate,
                toDate);
    }

    @Override
    public long countByStatus(StaffShiftStatus status) {
        return staffShiftJpaRepository.countByStatus(status);
    }

    @Override
    public Optional<StaffShift> findById(Long id) {
        return staffShiftJpaRepository.findById(id);
    }

    @Override
    public void save(StaffShift staffShift) {
        staffShiftJpaRepository.save(staffShift);
    }

    @Override
    public void saveAll(List<StaffShift> staffShifts) {
        staffShiftJpaRepository.saveAll(staffShifts);
    }

    @Override
    public void deleteById(Long id) {
        staffShiftJpaRepository.deleteById(id);
    }
}
