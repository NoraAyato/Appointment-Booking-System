package com.abs.app.domain.repository;

import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.ServiceImage;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.StaffShiftStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
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
    Page<ServiceEntity> search(String keyword, ServiceStatus status, String categoryId, Pageable pageable);
    Page<ServiceEntity> searchUserServices(
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
            Pageable pageable);
    List<ServiceImage> findImagesByServiceIds(List<String> serviceIds);
    Map<String, Double> findAverageRatingsByServiceIds(List<String> serviceIds);
    List<ServiceEntity> findTopRatedServices(ServiceStatus serviceStatus, ReviewsStatus reviewStatus, int limit);
}
