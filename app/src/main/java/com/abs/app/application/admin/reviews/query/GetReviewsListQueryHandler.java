package com.abs.app.application.admin.reviews.query;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.abs.app.application.admin.reviews.dto.ReviewsResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.repository.ReviewsRepository;
import com.abs.app.infrastructure.mapper.ReviewsMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetReviewsListQueryHandler {
    private final ReviewsRepository reviewsRepository;
    public PageResponse<ReviewsResponseDto> handle(GetReviewsListQuery query){
        List<Reviews> reviews = reviewsRepository.findAll();
        List<Reviews> filterReviews = reviews.stream()
                .filter(item ->(query.getKeyWord() == null
                        || item.getDescription().toLowerCase()
                        .contains(query.getKeyWord().toLowerCase()))
                        && (query.getStatus() == null ||
                        item.getStatus().name().equalsIgnoreCase(query.getStatus())))
                .collect(Collectors.toList());
        int total = filterReviews.size();
        List<ReviewsResponseDto> items = filterReviews.stream()
                .map(ReviewsMapper::toAdminReviewResponseDto)
                .collect(Collectors.toList());
        int page = query.getPage();
        int limit = query.getLimit();
        return new PageResponse<>(items, total, page, limit);
    }
}
