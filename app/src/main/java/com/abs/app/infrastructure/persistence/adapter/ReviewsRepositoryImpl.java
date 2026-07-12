package com.abs.app.infrastructure.persistence.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import com.abs.app.domain.repository.ReviewsRepository;
import com.abs.app.infrastructure.persistence.jpa.ReviewsJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewsRepositoryImpl implements ReviewsRepository {
    private final ReviewsJpaRepository reviewsJpaRepository;

    @Override
    public List<Reviews> findAll() {
        return reviewsJpaRepository.findAll();
    }

    @Override
    public Page<Reviews> search(String keyword, ReviewsStatus status, Pageable pageable) {
        return reviewsJpaRepository.search(keyword, status, pageable);
    }

    @Override
    public Optional<Reviews> findById(String id) {
        return reviewsJpaRepository.findById(id);
    }

    @Override
    public Map<String, Double> findAverageRatingsByStaffIds(List<String> staffIds, ReviewsStatus reviewStatus) {
        if (staffIds == null || staffIds.isEmpty()) {
            return Map.of();
        }

        Map<String, Double> ratings = new HashMap<>();
        reviewsJpaRepository.findAverageRatingsByStaffIds(staffIds, reviewStatus)
                .forEach(row -> ratings.put((String) row[0], ((Number) row[1]).doubleValue()));
        return ratings;
    }

    @Override
    public void save(Reviews reviews) {
        reviewsJpaRepository.save(reviews);
    }

    @Override
    public Page<Reviews> findByServiceId(String serviceId, Pageable pageable) {
        return reviewsJpaRepository.findByServiceId(serviceId, pageable);
    }

    @Override
    public Page<Reviews> findByServiceIdAndStatus(String serviceId, ReviewsStatus status, Pageable pageable) {
        return reviewsJpaRepository.findByServiceIdAndStatus(serviceId, status, pageable);
    }

    @Override
    public double findAverageRatingByServiceIdAndStatus(String serviceId, ReviewsStatus status) {
        Double averageRating = reviewsJpaRepository.findAverageRatingByServiceIdAndStatus(serviceId, status);
        return averageRating != null ? averageRating : 0D;
    }

    @Override
    public long countByServiceIdAndStatus(String serviceId, ReviewsStatus status) {
        return reviewsJpaRepository.countByServiceIdAndStatus(serviceId, status);
    }

    @Override
    public List<Object[]> findRatingDistributionByServiceIdAndStatus(String serviceId, ReviewsStatus status) {
        return reviewsJpaRepository.findRatingDistributionByServiceIdAndStatus(serviceId, status);
    }

    @Override
    public List<Reviews> findTopByStatus(ReviewsStatus status, int limit) {
        if (limit < 1) {
            return List.of();
        }
        return reviewsJpaRepository.findTopByStatus(status, PageRequest.of(0, limit));
    }

    @Override
    public long countByStatus(ReviewsStatus status) {
        return reviewsJpaRepository.countByStatus(status);
    }

    @Override
    public double findAverageRatingByStatus(ReviewsStatus status) {
        Double averageRating = reviewsJpaRepository.findAverageRatingByStatus(status);
        return averageRating != null ? averageRating : 0D;
    }
}
