package com.abs.app.application.admin.promotion.command;

import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletePromotionCommandHandler {
    private final PromotionRepository promotionRepository;

    public void handle(String id) {
        if (!promotionRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException(PromotionConstant.NOT_EXIST);
        }
        promotionRepository.deleteById(id);
    }
}
