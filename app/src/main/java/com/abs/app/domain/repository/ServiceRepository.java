package com.abs.app.domain.repository;

import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.enums.ServiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository {
    List<ServiceEntity> findAll();
    Optional<ServiceEntity> findById(String id);
    Optional<ServiceEntity> findByName(String name);
    ServiceEntity save(ServiceEntity service);
    void deleteById(String id);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndCategoryId(String name, String categoryId);
    Page<ServiceEntity> findByStatus(ServiceStatus status, Pageable pageable);
    Page<ServiceEntity> findByNameContainingIgnoreCaseAndStatus(String name, ServiceStatus status, Pageable pageable);
    Page<ServiceEntity> findByCategoryIdAndStatus(String categoryId, ServiceStatus status, Pageable pageable);
}
