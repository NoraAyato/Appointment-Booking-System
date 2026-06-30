package com.abs.app.presentation.controller.admin;

import com.abs.app.application.admin.reviews.query.GetReviewsListQueryHandler;
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
import com.abs.app.common.constant.ReviewsConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminReviewManagerController {

    private final GetReviewsListQueryHandler getReviewsListQueryHandler;
    private final ChangeReviewsStatusCommandHandler changeReviewsStatusCommandHandler;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ReviewsResponseDto>>> getReviewList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        
        GetReviewsListQuery query = new GetReviewsListQuery(page, limit, keyword, status);
        PageResponse<ReviewsResponseDto> reviewList = getReviewsListQueryHandler.handle(query);
        
        return ResponseEntity.ok(new ApiResponse<>(true, ReviewsConstant.GET_REVIEW_LIST_SUCCESS, reviewList));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateReviewStatus(
            @PathVariable String id,
            @RequestBody ChangeReviewsStatusRequest request) {
        
        ChangeReviewsStatusCommand command = new ChangeReviewsStatusCommand(id, request.getStatus());
        changeReviewsStatusCommandHandler.handle(command);
        
        return ResponseEntity.ok(new ApiResponse<>(true, ReviewsConstant.UPDATE_REVIEW_STATUS_SUCCESS, null));
    }
}
