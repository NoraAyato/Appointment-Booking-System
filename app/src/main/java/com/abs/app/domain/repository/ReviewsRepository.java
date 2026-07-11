package com.abs.app.domain.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.entity.enums.ReviewsStatus;

public interface ReviewsRepository {
    List<Reviews> findAll();

    Page<Reviews> search(String keyword, ReviewsStatus status, Pageable pageable);

    Page<Reviews> findByServiceId(String serviceId, Pageable pageable);

    Page<Reviews> findByServiceIdAndStatus(String serviceId, ReviewsStatus status, Pageable pageable);

    double findAverageRatingByServiceIdAndStatus(String serviceId, ReviewsStatus status);

    long countByServiceIdAndStatus(String serviceId, ReviewsStatus status);

    List<Object[]> findRatingDistributionByServiceIdAndStatus(String serviceId, ReviewsStatus status);

    List<Reviews> findTopByStatus(ReviewsStatus status, int limit);

    Optional<Reviews> findById(String id);

    Map<String, Double> findAverageRatingsByStaffIds(List<String> staffIds, ReviewsStatus reviewStatus);

    void save(Reviews review);
}
