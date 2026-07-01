package com.abs.app.domain.repository;

import com.abs.app.domain.entity.StaffService;

import java.util.List;
import java.util.Optional;

public interface StaffServiceRepository {
    List<StaffService> findAll();
    Optional<StaffService> findById(Long id);
    Optional<StaffService> findByStaffUserIdAndServiceId(String staffId, String staffServiceId);
    void save(StaffService staffService);
    void deleteById(Long id);
}
