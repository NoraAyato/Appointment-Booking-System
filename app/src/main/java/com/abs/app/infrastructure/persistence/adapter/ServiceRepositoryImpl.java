package com.abs.app.infrastructure.persistence.adapter;

import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.infrastructure.persistence.jpa.ServiceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceRepositoryImpl implements ServiceRepository {
    private final ServiceJpaRepository serviceJapRepository;

    @Override
    public List<com.abs.app.domain.entity.ServiceEntity> findAll() {
        return serviceJapRepository.findAll();
    }

    @Override
    public Optional<com.abs.app.domain.entity.ServiceEntity> findById(String id) {
        return serviceJapRepository.findById(id);
    }

    @Override
    public Optional<com.abs.app.domain.entity.ServiceEntity> findByName(String name) {
        return serviceJapRepository.findByName(name);
    }

    @Override
    public com.abs.app.domain.entity.ServiceEntity save(ServiceEntity service) {
        return serviceJapRepository.save(service);
    }

    @Override
    public void deleteById(String id) {
        serviceJapRepository.deleteById(id);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return serviceJapRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByNameIgnoreCaseAndCategoryId(String name, String categoryId) {
        return serviceJapRepository.existsByNameIgnoreCaseAndCategoryId(name, categoryId);
    }

    @Override
    public Page<com.abs.app.domain.entity.ServiceEntity> findByStatus(ServiceStatus status, Pageable pageable) {
        return serviceJapRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<com.abs.app.domain.entity.ServiceEntity> findByNameContainingIgnoreCaseAndStatus(String name, ServiceStatus status, Pageable pageable) {
        return serviceJapRepository.findByNameContainingIgnoreCaseAndStatus(name,status, pageable);
    }

    @Override
    public Page<com.abs.app.domain.entity.ServiceEntity> findByCategoryIdAndStatus(String categoryId, ServiceStatus status, Pageable pageable) {
        return serviceJapRepository.findByCategoryIdAndStatus(categoryId, status, pageable);
    }
}
