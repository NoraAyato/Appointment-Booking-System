package com.abs.app.domain.repository;

import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.enums.StaffServiceStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StaffServiceRepository {
    List<StaffService> findAll();
    Page<StaffService> search(String keyword, StaffServiceStatus status, Pageable pageable);
    Optional<StaffService> findById(Long id);
    Optional<StaffService> findByStaffUserIdAndServiceId(String staffId, String staffServiceId);
    void save(StaffService staffService);
    void deleteById(Long id);
}
