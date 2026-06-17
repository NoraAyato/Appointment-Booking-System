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
            String headString = command.getUserId() != null ? command.getUserId() : "promo";
            String storedImagePath = fileStorageService.storePromotion(command.getImage(), headString);
            promotion.setImage(storedImagePath);
        }

        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(PromotionConstant.USER_NOT_EXIST));
        promotion.setUser(user);

        promotionRepository.save(promotion);
        return PromotionMapper.toPromotionResponse(promotion);
    }
}
