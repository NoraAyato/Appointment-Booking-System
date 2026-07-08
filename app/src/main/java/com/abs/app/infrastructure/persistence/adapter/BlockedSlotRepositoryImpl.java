package com.abs.app.infrastructure.persistence.adapter;

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
    public void save(BlockedSlot blockedSlot) {
        blockedSlotJpaRepository.save(blockedSlot);
    }

    @Override
    public void deleteById(Long id) {
        blockedSlotJpaRepository.deleteById(id);
    }

}
