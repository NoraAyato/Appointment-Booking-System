package com.abs.app.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.abs.app.domain.entity.BlockedSlot;
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
    public void save(BlockedSlot blockedSlot) {
        blockedSlotJpaRepository.save(blockedSlot);
    }

    @Override
    public void deleteById(Long id) {
        blockedSlotJpaRepository.deleteById(id);
    }

}
