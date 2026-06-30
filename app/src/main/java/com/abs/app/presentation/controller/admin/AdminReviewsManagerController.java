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

import com.abs.app.application.admin.reviewmanager.command.ChangeReviewStatusCommand;
import com.abs.app.application.admin.reviewmanager.command.ChangeReviewStatusCommandHandler;
import com.abs.app.application.admin.reviewmanager.dto.ChangeReviewStatusRequest;
import com.abs.app.application.admin.reviewmanager.dto.ReviewResponseDto;
import com.abs.app.application.admin.reviewmanager.query.GetReviewListQuery;
import com.abs.app.application.admin.reviewmanager.query.GetReviewListQueryHandler;
import com.abs.app.common.constant.ReviewConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminReviewManagerController {
    private final GetReviewListQueryHandler getReviewListQueryHandler;
    private final ChangeReviewStatusCommandHandler changeReviewStatusCommandHandler;
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ReviewResponseDto>>> getReviews(
            @RequestParam(required = false) String keyWord,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PageResponse<ReviewResponseDto> reviews = getReviewListQueryHandler
                .handle(new GetReviewListQuery(keyWord, status, page, limit));
        return ResponseEntity.ok(new ApiResponse<>(true, ReviewConstant.GET_REVIEW_SLOTS_SUCCESS,
                reviews));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<Void>> updateReviews(@PathVariable String id,
                                                           @RequestBody ChangeReviewStatusRequest request){
        changeReviewStatusCommandHandler.handle(new ChangeReviewStatusCommand(id, request.getStatus()));
        return ResponseEntity.ok(new ApiResponse<>(true, ReviewConstant.UPDATE_REVIEW_SUCCESS, null));
    }
}
