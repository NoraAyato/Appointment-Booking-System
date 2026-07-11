package com.abs.app.application.user.reviews.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.user.reviews.dto.TopReviewsResponseDto;
import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import com.abs.app.domain.repository.ReviewsRepository;
import com.abs.app.infrastructure.mapper.ReviewsMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetTopReviewsQueryHandler {
    private static final int TOP_REVIEWS_LIMIT = 3;

    private final ReviewsRepository reviewsRepository;

    @Transactional(readOnly = true)
    public List<TopReviewsResponseDto> handle(GetTopReviewsQuery query) {
        List<Reviews> reviews = reviewsRepository.findTopByStatus(
                ReviewsStatus.APPROVED,
                TOP_REVIEWS_LIMIT);

        return reviews.stream()
                .map(ReviewsMapper::toTopReviewsResponseDto)
                .toList();
    }
}
