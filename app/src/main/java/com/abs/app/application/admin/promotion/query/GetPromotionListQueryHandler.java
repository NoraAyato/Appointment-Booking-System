package com.abs.app.application.admin.promotion.query;

import com.abs.app.application.admin.promotion.dto.PromotionResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.EnumUtil;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.entity.enums.DiscountType;
import com.abs.app.domain.entity.enums.PromotionStatus;
import com.abs.app.domain.repository.PromotionRepository;
import com.abs.app.infrastructure.mapper.PromotionMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetPromotionListQueryHandler {
        private final PromotionRepository promotionRepository;

        public PageResponse<PromotionResponseDto> handle(GetPromotionListQuery query) {
                Pageable pageable = PaginationUtil.createPageable(
                                query.getPage(),
                                query.getSize(),
                                Sort.by("startDate").descending().and(Sort.by("id").ascending()));
                Optional<PromotionStatus> status = EnumUtil.parse(PromotionStatus.class, query.getStatus());
                Optional<DiscountType> discountType = EnumUtil.parse(DiscountType.class, query.getDiscountType());
                if (EnumUtil.isInvalidEnumValue(query.getStatus(), status)
                                || EnumUtil.isInvalidEnumValue(query.getDiscountType(), discountType)) {
                        return PaginationUtil.emptyResponse(query.getPage(), query.getSize());
                }

                Page<Promotion> promotions = promotionRepository.search(
                                query.getKeyword(),
                                status.orElse(null),
                                discountType.orElse(null),
                                query.getFromDate(),
                                query.getToDate(),
                                pageable);

                return PaginationUtil.toPageResponse(
                                promotions,
                                PromotionMapper::toPromotionResponse,
                                query.getPage(),
                                query.getSize());
        }
}
