package com.abs.app.domain.repository;

import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.enums.StaffShiftStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StaffShiftRepository {
    List<StaffShift> findAll();
    Page<StaffShift> search(String keyword, StaffShiftStatus status, Pageable pageable);
    Page<StaffShift> searchByStaffId(String staffId, String keyword, StaffShiftStatus status, Pageable pageable);
    Optional<StaffShift> findById(Long id);
    void save(StaffShift staffShift);
    void deleteById(Long id);
}
