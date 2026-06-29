package com.abs.app.domain.repository;

import java.util.List;
import java.util.Optional;

import com.abs.app.domain.entity.BlockedSlot;

public interface BlockedSlotRepository {
    Optional<BlockedSlot> findById(Long id);

    List<BlockedSlot> findAll();

    BlockedSlot save(BlockedSlot blockedSlot);

    void deleteById(Long id);
}
