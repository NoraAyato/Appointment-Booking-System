package com.abs.app.infrastructure.persistence.adapter;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import com.abs.app.domain.repository.ReviewsRepository;
import com.abs.app.infrastructure.persistence.jpa.ReviewsJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewsRepositoryImpl implements ReviewsRepository {
    private final ReviewsJpaRepository reviewsJpaRepository;
    public List<Reviews> findAll(){
        return reviewsJpaRepository.findAll();
    }
    public Page<Reviews> search(String keyword, ReviewsStatus status, Pageable pageable) {
        return reviewsJpaRepository.search(keyword, status, pageable);
    }
    public Optional<Reviews> findById(String id){
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

    public void save(Reviews reviews){
        reviewsJpaRepository.save(reviews);
    }
}
