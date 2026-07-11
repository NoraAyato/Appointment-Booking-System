package com.abs.app.domain.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.entity.enums.ReviewsStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewsRepository {
    List<Reviews> findAll();
    Page<Reviews> search(String keyword, ReviewsStatus status, Pageable pageable);
    
    Optional<Reviews> findById(String id);
    
    Map<String, Double> findAverageRatingsByStaffIds(List<String> staffIds, ReviewsStatus reviewStatus);

    void save(Reviews review);
}
