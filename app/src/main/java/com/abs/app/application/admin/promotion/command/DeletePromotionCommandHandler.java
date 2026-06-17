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
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with id: " + id));
        promotionRepository.deleteById(promotion.getId());
    }
}
