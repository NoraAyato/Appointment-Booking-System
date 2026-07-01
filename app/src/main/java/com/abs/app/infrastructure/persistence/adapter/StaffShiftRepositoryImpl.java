package com.abs.app.infrastructure.persistence.adapter;

import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.repository.StaffShiftRepository;
import com.abs.app.infrastructure.persistence.jpa.StaffShiftJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Optional<StaffShift> findById(Long id) {
        return staffShiftJpaRepository.findById(id);
    }

    @Override
    public void save(StaffShift staffShift) {
        staffShiftJpaRepository.save(staffShift);
    }

    @Override
    public void deleteById(Long id) {
        staffShiftJpaRepository.deleteById(id);
    }
}
