package com.abs.app.application.user.promotion.query;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.user.promotion.dto.PublicPromotionResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.entity.enums.PromotionStatus;
import com.abs.app.domain.repository.PromotionRepository;
import com.abs.app.infrastructure.mapper.PromotionMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetPublicPromotionsQueryHandler {
    private final PromotionRepository promotionRepository;

    @Transactional(readOnly = true)
    public PageResponse<PublicPromotionResponseDto> handle(GetPublicPromotionsQuery query) {
        Pageable pageable = PaginationUtil.createPageable(
                query.getPage(),
                query.getLimit(),
                Sort.by("endDate").ascending().and(Sort.by("id").ascending()));

        Page<Promotion> promotions = promotionRepository.findAvailablePromotions(
                PromotionStatus.ACTIVE,
                LocalDate.now(),
                pageable);

        return PaginationUtil.toPageResponse(
                promotions,
                PromotionMapper::toPublicPromotionResponse,
                query.getPage(),
                query.getLimit());
    }
}
