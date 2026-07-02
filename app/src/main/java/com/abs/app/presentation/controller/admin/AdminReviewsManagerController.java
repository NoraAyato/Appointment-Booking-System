package com.abs.app.presentation.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.admin.reviews.command.ChangeReviewsStatusCommand;
import com.abs.app.application.admin.reviews.command.ChangeReviewsStatusCommandHandler;
import com.abs.app.application.admin.reviews.dto.ChangeReviewsStatusRequest;
import com.abs.app.application.admin.reviews.dto.ReviewsResponseDto;
import com.abs.app.application.admin.reviews.query.GetReviewsListQuery;
import com.abs.app.application.admin.reviews.query.GetReviewsListQueryHandler;
import com.abs.app.common.constant.ReviewsConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminReviewsManagerController {
    private final GetReviewsListQueryHandler getReviewListQueryHandler;
    private final ChangeReviewsStatusCommandHandler changeReviewStatusCommandHandler;
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ReviewsResponseDto>>> getReviews(
            @RequestParam(required = false) String keyWord,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PageResponse<ReviewsResponseDto> reviews = getReviewListQueryHandler
                .handle(new GetReviewsListQuery(keyWord, status, page, limit));
        return ResponseEntity.ok(new ApiResponse<>(true, ReviewsConstant.GET_REVIEW_SLOTS_SUCCESS,
                reviews));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<Void>> updateReviews(@PathVariable String id,
                                                           @RequestBody ChangeReviewsStatusRequest request){
        changeReviewStatusCommandHandler.handle(new ChangeReviewsStatusCommand(id, request.getStatus()));
        return ResponseEntity.ok(new ApiResponse<>(true, ReviewsConstant.UPDATE_REVIEW_SUCCESS, null));
    }
}
