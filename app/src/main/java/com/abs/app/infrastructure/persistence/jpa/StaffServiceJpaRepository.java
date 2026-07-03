package com.abs.app.infrastructure.persistence.jpa;

import com.abs.app.domain.entity.StaffService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffServiceJpaRepository extends JpaRepository<StaffService, Long> {
    Optional<StaffService> findByStaffUserIdAndServiceId(String staffId, String staffServiceId);
}
