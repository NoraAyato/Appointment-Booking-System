package com.abs.app.application.user.reviews.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceReviewsStatsResponseDto {
    private double averageRating;
    private long totalReviews;
    private List<RatingDistribution> ratingDistribution;
}
