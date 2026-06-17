package com.abs.app.application.admin.promotion.query;

import com.abs.app.application.admin.promotion.dto.PromotionResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.repository.PromotionRepository;
import com.abs.app.infrastructure.mapper.PromotionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPromotionListQueryHandler {

    private final PromotionRepository promotionRepository;

    public PageResponse<PromotionResponseDto> handle(GetPromotionListQuery query) {
        int page = query.getPage() > 0 ? query.getPage() - 1 : 0;
        Pageable pageable = PageRequest.of(page, query.getSize());
        Page<Promotion> promotions;

        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            promotions = promotionRepository.findByDescriptionContaining(query.getKeyword(), pageable);
        } else {
            promotions = promotionRepository.findAll(pageable);
        }

        List<PromotionResponseDto> items = promotions.map(PromotionMapper::toPromotionResponse).getContent();
        return new PageResponse<>(items, (int) promotions.getTotalElements(), query.getPage(), query.getSize());
    }
}
