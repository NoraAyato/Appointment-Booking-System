package com.abs.app.domain.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlockedSlotRepository {
    Optional<BlockedSlot> findById(Long id);

    List<BlockedSlot> findAll();
    Page<BlockedSlot> search(String keyword, BlockedSlotStatus status, Pageable pageable);
    Page<BlockedSlot> searchByStaffId(String staffId, String keyword, BlockedSlotStatus status, Pageable pageable);
    boolean existsOverlapping(
            String staffId,
            LocalDate blockedDate,
            LocalTime startTime,
            LocalTime endTime,
            BlockedSlotStatus excludedStatus);
    boolean existsOverlappingByStatus(
            String staffId,
            LocalDate blockedDate,
            LocalTime startTime,
            LocalTime endTime,
            BlockedSlotStatus status);
    boolean existsAllDayOnDateByStatus(
            String staffId,
            LocalDate blockedDate,
            BlockedSlotStatus status);

    List<BlockedSlot> findApprovedVisibleToStaffInDateRange(
            String staffId,
            LocalDate fromDate,
            LocalDate toDate);

    List<BlockedSlot> findApprovedByStaffIdInDateRange(
            String staffId,
            LocalDate fromDate,
            LocalDate toDate);

    long countByStatus(BlockedSlotStatus status);

    void save(BlockedSlot blockedSlot);

    void deleteById(Long id);
}
