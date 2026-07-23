package com.abs.app.application.user.promotion.query;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.user.promotion.dto.AvailablePromotionResponseDto;
import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.entity.enums.PromotionStatus;
import com.abs.app.domain.repository.PromotionRepository;
import com.abs.app.infrastructure.mapper.PromotionMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetAvailablePromotionsQueryHandler {
    private final PromotionRepository promotionRepository;

    @Transactional(readOnly = true)
    public List<AvailablePromotionResponseDto> handle() {
        List<Promotion> promotions = promotionRepository.findAvailablePromotions(
                PromotionStatus.ACTIVE,
                LocalDate.now());

        return promotions.stream()
                .map(PromotionMapper::toAvailablePromotionResponse)
                .toList();
    }
}
