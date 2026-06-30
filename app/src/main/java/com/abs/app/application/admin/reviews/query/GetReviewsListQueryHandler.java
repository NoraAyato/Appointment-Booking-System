package com.abs.app.application.admin.reviewmanager.query;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.abs.app.application.admin.reviewmanager.dto.ReviewResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.repository.ReviewRepository;
import com.abs.app.infrastructure.mapper.ReviewMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetReviewListQueryHandler {
    private final ReviewRepository reviewsRepository;
    public PageResponse<ReviewResponseDto> handle(GetReviewListQuery query){
        List<Reviews> reviews = reviewsRepository.findAll();
        List<Reviews> filterReviews = reviews.stream()
                .filter(item ->(query.getKeyWord() == null
                        || item.getDescription().toLowerCase()
                        .contains(query.getKeyWord().toLowerCase()))
                        && (query.getStatus() == null ||
                        item.getStatus().name().equalsIgnoreCase(query.getStatus())))
                .collect(Collectors.toList());
        int total = filterReviews.size();
        List<ReviewResponseDto> items = filterReviews.stream()
                .map(ReviewMapper::toAdminReviewResponseDto)
                .collect(Collectors.toList());
        int page = query.getPage();
        int limit = query.getLimit();
        return new PageResponse<>(items, total, page, limit);
    }
}
