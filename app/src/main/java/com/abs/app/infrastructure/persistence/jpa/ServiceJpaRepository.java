package com.abs.app.infrastructure.persistence.jpa;

import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.enums.ServiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceJpaRepository extends JpaRepository<ServiceEntity, String> {
    Optional<ServiceEntity> findByName(String name);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndCategoryId(String name, String categoryId);
    Page<ServiceEntity> findByStatus(ServiceStatus status, Pageable pageable);
    Page<ServiceEntity> findByNameContainingIgnoreCaseAndStatus(String name, ServiceStatus status, Pageable pageable);
    Page<ServiceEntity> findByCategoryIdAndStatus(String categoryId, ServiceStatus status, Pageable pageable);
}
