package com.abs.app.presentation.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.user.reviews.dto.ServiceReviewsResponseDto;
import com.abs.app.application.user.reviews.dto.ServiceReviewsStatsResponseDto;
import com.abs.app.application.user.reviews.dto.TopReviewsResponseDto;
import com.abs.app.application.user.reviews.query.GetServiceReviewsQuery;
import com.abs.app.application.user.reviews.query.GetServiceReviewsQueryHandler;
import com.abs.app.application.user.reviews.query.GetServiceReviewsStatsQuery;
import com.abs.app.application.user.reviews.query.GetServiceReviewsStatsQueryHandler;
import com.abs.app.application.user.reviews.query.GetTopReviewsQuery;
import com.abs.app.application.user.reviews.query.GetTopReviewsQueryHandler;
import com.abs.app.common.constant.ReviewsConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/public/reviews")
@RequiredArgsConstructor
public class ReviewsController {
    private final GetServiceReviewsQueryHandler getServiceReviewsQueryHandler;
    private final GetServiceReviewsStatsQueryHandler getServiceReviewsStatsQueryHandler;
    private final GetTopReviewsQueryHandler getTopReviewsQueryHandler;

    @GetMapping("/top-rated")
    public ResponseEntity<ApiResponse<List<TopReviewsResponseDto>>> getTopReviews() {
        List<TopReviewsResponseDto> reviews = getTopReviewsQueryHandler.handle(new GetTopReviewsQuery());

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                ReviewsConstant.GET_TOP_REVIEWS_SUCCESS,
                reviews));
    }

    @GetMapping("/services/{serviceId}/stats")
    public ResponseEntity<ApiResponse<ServiceReviewsStatsResponseDto>> getServiceReviewsStats(
            @PathVariable String serviceId) {
        ServiceReviewsStatsResponseDto stats = getServiceReviewsStatsQueryHandler
                .handle(new GetServiceReviewsStatsQuery(serviceId));

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                ReviewsConstant.GET_SERVICE_REVIEW_STATS_SUCCESS,
                stats));
    }

    @GetMapping("/services/{serviceId}")
    public ResponseEntity<ApiResponse<PageResponse<ServiceReviewsResponseDto>>> getServiceReviews(
            @PathVariable String serviceId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PageResponse<ServiceReviewsResponseDto> reviews = getServiceReviewsQueryHandler
                .handle(new GetServiceReviewsQuery(serviceId, page, limit));

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                ReviewsConstant.GET_SERVICE_REVIEWS_SUCCESS,
                reviews));
    }
}
