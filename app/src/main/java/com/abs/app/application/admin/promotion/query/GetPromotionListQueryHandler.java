package com.abs.app.application.admin.promotion.query;

import com.abs.app.application.admin.promotion.dto.PromotionResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.repository.PromotionRepository;
import com.abs.app.infrastructure.mapper.PromotionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPromotionListQueryHandler {

    private final PromotionRepository promotionRepository;

    public PageResponse<PromotionResponseDto> handle(GetPromotionListQuery query) {
        List<Promotion> promotions = promotionRepository.searchPromotions(query.getKeyword(), query.getStartDate(), query.getEndDate(), query.getActive());
        
        List<Promotion> paginatedPromotions = PaginationUtil.paginate(promotions, query.getPage(), query.getSize());
        
        List<PromotionResponseDto> items = paginatedPromotions.stream().map(PromotionMapper::toPromotionResponse).toList();
        
        return new PageResponse<>(items, promotions.size(), query.getPage(), query.getSize());
    }
}
