package com.abs.app.application.user.reviews.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.user.reviews.dto.ServiceReviewsResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import com.abs.app.domain.repository.ReviewsRepository;
import com.abs.app.infrastructure.mapper.ReviewsMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetServiceReviewsQueryHandler {
    private final ReviewsRepository reviewsRepository;

    @Transactional(readOnly = true)
    public PageResponse<ServiceReviewsResponseDto> handle(GetServiceReviewsQuery query) {
        Pageable pageable = PaginationUtil.createPageable(query.getPage(), query.getLimit());
        Page<Reviews> reviews = reviewsRepository.findByServiceIdAndStatus(
                query.getServiceId(),
                ReviewsStatus.APPROVED,
                pageable);

        return PaginationUtil.toPageResponse(
                reviews,
                review -> ReviewsMapper.toServiceReviewsResponseDto(review, query.getServiceId()),
                query.getPage(),
                query.getLimit());
    }
}
