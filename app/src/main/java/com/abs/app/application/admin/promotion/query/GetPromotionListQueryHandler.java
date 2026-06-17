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
        List<Promotion> promotionList = promotionRepository.findAll();
        List<Promotion> filteredList = promotionList.stream()
                .filter(promotion -> query.getKeyword() == null || promotion.getDescription().toLowerCase().contains(query.getKeyword().toLowerCase()))
                .filter(promotion -> query.getActive() == null || promotion.getActive().equals(query.getActive() == 1))
                .filter(promotion -> query.getFromDate() == null || !promotion.getStartDate().isBefore(query.getFromDate()))
                .filter(promotion -> query.getToDate() == null || !promotion.getEndDate().isAfter(query.getToDate()))
                .toList();

        int total = filteredList.size();
        List<Promotion> pageFilterList = PaginationUtil.paginate(filteredList, query.getPage(), query.getSize());

        List<PromotionResponseDto> items = pageFilterList.stream()
                .map(PromotionMapper::toPromotionResponse)
                .toList();

        return new PageResponse<>(items, total, query.getPage(), query.getSize());
    }
}
