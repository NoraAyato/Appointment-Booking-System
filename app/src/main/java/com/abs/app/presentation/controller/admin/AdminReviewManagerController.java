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
    public ResponseEntity<ApiResponse<PageResponse<ReviewResponseDto>>> getReviewList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        
        GetReviewListQuery query = new GetReviewListQuery(page, limit, keyword, status);
        PageResponse<ReviewResponseDto> reviewList = getReviewListQueryHandler.handle(query);
        
        return ResponseEntity.ok(new ApiResponse<>(true, ReviewConstant.GET_REVIEW_LIST_SUCCESS, reviewList));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateReviewStatus(
            @PathVariable String id,
            @RequestBody ChangeReviewStatusRequest request) {
        
        ChangeReviewStatusCommand command = new ChangeReviewStatusCommand(id, request.getStatus());
        changeReviewStatusCommandHandler.handle(command);
        
        return ResponseEntity.ok(new ApiResponse<>(true, ReviewConstant.UPDATE_REVIEW_STATUS_SUCCESS, null));
    }
}
