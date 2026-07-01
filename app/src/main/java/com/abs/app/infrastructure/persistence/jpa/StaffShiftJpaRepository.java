package com.abs.app.infrastructure.persistence.jpa;

import com.abs.app.domain.entity.StaffShift;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StaffShiftJpaRepository extends JpaRepository<StaffShift, Long> {
}
