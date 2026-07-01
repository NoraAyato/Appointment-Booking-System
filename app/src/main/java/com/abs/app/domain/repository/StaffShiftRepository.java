package com.abs.app.domain.repository;

import com.abs.app.domain.entity.StaffShift;

import java.util.List;
import java.util.Optional;

public interface StaffShiftRepository {
    List<StaffShift> findAll();
    Optional<StaffShift> findById(Long id);
    void save(StaffShift staffShift);
    void deleteById(Long id);
}
