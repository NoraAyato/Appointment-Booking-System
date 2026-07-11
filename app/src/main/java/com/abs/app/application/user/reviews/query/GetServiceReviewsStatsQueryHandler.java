package com.abs.app.application.user.reviews.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.user.reviews.dto.ServiceReviewsStatsResponseDto;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import com.abs.app.domain.repository.ReviewsRepository;
import com.abs.app.infrastructure.mapper.ReviewsMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetServiceReviewsStatsQueryHandler {
    private final ReviewsRepository reviewsRepository;

    @Transactional(readOnly = true)
    public ServiceReviewsStatsResponseDto handle(GetServiceReviewsStatsQuery query) {
        double averageRating = reviewsRepository.findAverageRatingByServiceIdAndStatus(
                query.getServiceId(),
                ReviewsStatus.APPROVED);
        long totalReviews = reviewsRepository.countByServiceIdAndStatus(
                query.getServiceId(),
                ReviewsStatus.APPROVED);
        List<Object[]> ratingRows = reviewsRepository.findRatingDistributionByServiceIdAndStatus(
                query.getServiceId(),
                ReviewsStatus.APPROVED);

        return ReviewsMapper.toServiceReviewsStatsResponseDto(averageRating, totalReviews, ratingRows);
    }
}
