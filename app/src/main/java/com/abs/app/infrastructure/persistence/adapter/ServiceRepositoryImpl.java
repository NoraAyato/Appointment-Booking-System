package com.abs.app.infrastructure.persistence.adapter;

import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.ServiceImage;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.infrastructure.persistence.jpa.ServiceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public Page<ServiceEntity> search(String keyword, ServiceStatus status, String categoryId, Pageable pageable) {
        return serviceJapRepository.search(keyword, status, categoryId, pageable);
    }

    @Override
    public Page<ServiceEntity> searchUserServices(
            String keyword,
            String categoryId,
            LocalDate date,
            LocalTime time,
            LocalDateTime requestedStartAt,
            ServiceStatus serviceStatus,
            StaffServiceStatus staffServiceStatus,
            StaffShiftStatus staffShiftStatus,
            BlockedSlotStatus blockedSlotStatus,
            AppointmentStatus excludedAppointmentStatus,
            Pageable pageable) {
        return serviceJapRepository.searchUserServices(
                keyword,
                categoryId,
                date,
                time,
                requestedStartAt,
                serviceStatus.name(),
                staffServiceStatus.name(),
                staffShiftStatus.name(),
                blockedSlotStatus.name(),
                excludedAppointmentStatus.name(),
                pageable);
    }

    @Override
    public List<ServiceImage> findImagesByServiceIds(List<String> serviceIds) {
        if (serviceIds == null || serviceIds.isEmpty()) {
            return List.of();
        }
        return serviceJapRepository.findImagesByServiceIds(serviceIds);
    }

    @Override
    public Map<String, Double> findAverageRatingsByServiceIds(List<String> serviceIds) {
        if (serviceIds == null || serviceIds.isEmpty()) {
            return Map.of();
        }

        Map<String, Double> ratings = new HashMap<>();
        serviceJapRepository.findAverageRatingsByServiceIds(serviceIds)
                .forEach(row -> ratings.put((String) row[0], ((Number) row[1]).doubleValue()));
        return ratings;
    }

    @Override
    public List<ServiceEntity> findTopRatedServices(ServiceStatus serviceStatus, ReviewsStatus reviewStatus, int limit) {
        if (limit < 1) {
            return List.of();
        }
        return serviceJapRepository.findTopRatedServices(
                serviceStatus.name(),
                reviewStatus.name(),
                PageRequest.of(0, limit));
    }

    @Override
    public long countByStatusValue(ServiceStatus status) {
        return serviceJapRepository.countByStatus(status);
    }

    @Override
    public long countActiveServicesWithoutActiveStaff() {
        return serviceJapRepository.countActiveServicesWithoutActiveStaff();
    }
}
