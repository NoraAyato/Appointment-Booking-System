package com.abs.app.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abs.app.domain.entity.BlockedSlot;

public interface BlockedSlotJpaRepository extends JpaRepository<BlockedSlot, Long> {

}
