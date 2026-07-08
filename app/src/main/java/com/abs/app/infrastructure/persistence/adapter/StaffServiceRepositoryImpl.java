package com.abs.app.infrastructure.persistence.adapter;

import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.infrastructure.persistence.jpa.StaffServiceJpaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public void save(StaffService staffService) {
        staffServiceJpaRepository.save(staffService);
    }

    @Override
    public void deleteById(Long id) {
        staffServiceJpaRepository.deleteById(id);
    }
}
