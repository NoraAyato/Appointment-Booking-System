package com.abs.app.application.admin.promotion.command;

import com.abs.app.application.admin.promotion.dto.PromotionResponseDto;
import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.repository.PromotionRepository;
import com.abs.app.infrastructure.file.FileStorageService;
import com.abs.app.infrastructure.mapper.PromotionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreatePromotionCommandHandler {

    private final PromotionRepository promotionRepository;
    private final FileStorageService fileStorageService;

    public PromotionResponseDto handle(CreatePromotionCommand command) {
        Promotion promotion = new Promotion();
        promotion.setId(UUID.randomUUID().toString());
        promotion.setDescription(command.getDescription());
        promotion.setDiscountAmount(command.getDiscountAmount());
        promotion.setDiscountType(command.getDiscountType());
        promotion.setActive(command.getActive() != null ? command.getActive() : true);
        promotion.setStartDate(command.getStartDate());
        promotion.setEndDate(command.getEndDate());

        if (command.getImage() != null && !command.getImage().isEmpty()) {
            String imagePath = fileStorageService.storePromotion(command.getImage(), com.abs.app.common.constant.PromotionConstant.SALT_TAG + "_" + promotion.getId());
            promotion.setImage(imagePath);
        }

        Promotion saved = promotionRepository.save(promotion);
        return PromotionMapper.toPromotionResponse(saved);
    }
}
