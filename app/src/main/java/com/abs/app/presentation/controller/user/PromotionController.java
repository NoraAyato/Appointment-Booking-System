package com.abs.app.presentation.controller.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.user.promotion.dto.AvailablePromotionResponseDto;
import com.abs.app.application.user.promotion.dto.PublicPromotionResponseDto;
import com.abs.app.application.user.promotion.query.GetAvailablePromotionsQueryHandler;
import com.abs.app.application.user.promotion.query.GetPublicPromotionsQuery;
import com.abs.app.application.user.promotion.query.GetPublicPromotionsQueryHandler;
import com.abs.app.common.constant.PromotionConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/public/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final GetAvailablePromotionsQueryHandler getAvailablePromotionsQueryHandler;
    private final GetPublicPromotionsQueryHandler getPublicPromotionsQueryHandler;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PublicPromotionResponseDto>>> getPromotions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PageResponse<PublicPromotionResponseDto> promotions = getPublicPromotionsQueryHandler.handle(
                new GetPublicPromotionsQuery(page, limit));

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                PromotionConstant.GET_PUBLIC_PROMOTIONS_SUCCESS,
                promotions));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<AvailablePromotionResponseDto>>> getAvailablePromotions() {
        List<AvailablePromotionResponseDto> promotions = getAvailablePromotionsQueryHandler.handle();

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                PromotionConstant.GET_AVAILABLE_SUCCESS,
                promotions));
    }
}
