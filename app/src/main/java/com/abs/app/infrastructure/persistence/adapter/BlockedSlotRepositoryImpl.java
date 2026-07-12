package com.abs.app.infrastructure.persistence.adapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.repository.BlockedSlotRepository;
import com.abs.app.infrastructure.persistence.jpa.BlockedSlotJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockedSlotRepositoryImpl implements BlockedSlotRepository {

    private final BlockedSlotJpaRepository blockedSlotJpaRepository;

    @Override
    public Optional<BlockedSlot> findById(Long id) {
        return blockedSlotJpaRepository.findById(id);
    }

    @Override
    public List<BlockedSlot> findAll() {
        return blockedSlotJpaRepository.findAll();

    }

    @Override
    public Page<BlockedSlot> search(String keyword, BlockedSlotStatus status, Pageable pageable) {
        return blockedSlotJpaRepository.search(keyword, status, pageable);
    }

    @Override
    public Page<BlockedSlot> searchByStaffId(String staffId, String keyword, BlockedSlotStatus status, Pageable pageable) {
        return blockedSlotJpaRepository.searchByStaffId(staffId, keyword, status, pageable);
    }

    @Override
    public boolean existsOverlapping(
            String staffId,
            LocalDate blockedDate,
            LocalTime startTime,
            LocalTime endTime,
            BlockedSlotStatus excludedStatus) {
        return blockedSlotJpaRepository.existsOverlapping(
                staffId,
                blockedDate,
                startTime,
                endTime,
                excludedStatus);
    }

    @Override
    public boolean existsOverlappingByStatus(
            String staffId,
            LocalDate blockedDate,
            LocalTime startTime,
            LocalTime endTime,
            BlockedSlotStatus status) {
        return blockedSlotJpaRepository.existsOverlappingByStatus(
                staffId,
                blockedDate,
                startTime,
                endTime,
                status);
    }

    @Override
    public boolean existsAllDayOnDateByStatus(
            String staffId,
            LocalDate blockedDate,
            BlockedSlotStatus status) {
        return blockedSlotJpaRepository.existsAllDayOnDateByStatus(
                staffId,
                blockedDate,
                status);
    }

    @Override
    public long countByStatus(BlockedSlotStatus status) {
        return blockedSlotJpaRepository.countByStatus(status);
    }

    @Override
    public void save(BlockedSlot blockedSlot) {
        blockedSlotJpaRepository.save(blockedSlot);
    }

    @Override
    public void deleteById(Long id) {
        blockedSlotJpaRepository.deleteById(id);
    }

}
