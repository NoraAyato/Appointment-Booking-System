package com.abs.app.application.admin.promotion.command;

import com.abs.app.common.constant.PromotionConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.entity.enums.PromotionStatus;
import com.abs.app.domain.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletePromotionCommandHandler {
    private final PromotionRepository promotionRepository;

    public void handle(String id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PromotionConstant.NOT_EXIST));
        promotion.setStatus(PromotionStatus.DELETED);
        promotionRepository.save(promotion);
    }
}
