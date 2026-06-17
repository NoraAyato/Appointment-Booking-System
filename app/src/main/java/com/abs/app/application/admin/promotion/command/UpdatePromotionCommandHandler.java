package com.abs.app.application.admin.promotion.command;

import com.abs.app.application.admin.promotion.dto.PromotionResponseDto;
import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.repository.PromotionRepository;
import com.abs.app.infrastructure.file.FileStorageService;
import com.abs.app.infrastructure.mapper.PromotionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatePromotionCommandHandler {

    private final PromotionRepository promotionRepository;
    private final FileStorageService fileStorageService;

    public PromotionResponseDto handle(UpdatePromotionCommand command) {
        Promotion promotion = promotionRepository.findById(command.getId())
                .orElseThrow(() -> new RuntimeException("Promotion not found with id: " + command.getId()));

        if (command.getDescription() != null) promotion.setDescription(command.getDescription());
        if (command.getDiscountAmount() != null) promotion.setDiscountAmount(command.getDiscountAmount());
        if (command.getDiscountType() != null) promotion.setDiscountType(command.getDiscountType());
        if (command.getActive() != null) promotion.setActive(command.getActive());
        if (command.getStartDate() != null) promotion.setStartDate(command.getStartDate());
        if (command.getEndDate() != null) promotion.setEndDate(command.getEndDate());

        if (command.getImage() != null && !command.getImage().isEmpty()) {
            String imagePath = fileStorageService.storePromotion(command.getImage(), com.abs.app.common.constant.PromotionConstant.SALT_TAG + "_" + promotion.getId());
            promotion.setImage(imagePath);
        }

        Promotion updated = promotionRepository.save(promotion);
        return PromotionMapper.toPromotionResponse(updated);
    }
}
