package com.abs.app.application.admin.reviews.query;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.admin.reviews.dto.ReviewsResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.EnumUtil;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import com.abs.app.domain.repository.ReviewsRepository;
import com.abs.app.infrastructure.mapper.ReviewsMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetReviewsListQueryHandler {
    private final ReviewsRepository reviewsRepository;
    @Transactional(readOnly = true)
    public PageResponse<ReviewsResponseDto> handle(GetReviewsListQuery query){
        Pageable pageable = PaginationUtil.createPageable(
                query.getPage(),
                query.getLimit(),
                Sort.by("createAt").descending());
        Optional<ReviewsStatus> status = EnumUtil.parse(ReviewsStatus.class, query.getStatus());
        if (EnumUtil.isInvalidEnumValue(query.getStatus(), status)) {
            return PaginationUtil.emptyResponse(query.getPage(), query.getLimit());
        }

        Page<Reviews> reviews = reviewsRepository.search(query.getKeyWord(), status.orElse(null), pageable);

        return PaginationUtil.toPageResponse(
                reviews,
                ReviewsMapper::toAdminReviewResponseDto,
                query.getPage(),
                query.getLimit());
    }
}
