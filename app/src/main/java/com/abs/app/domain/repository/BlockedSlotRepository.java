package com.abs.app.domain.repository;

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

    void save(BlockedSlot blockedSlot);

    void deleteById(Long id);
}
