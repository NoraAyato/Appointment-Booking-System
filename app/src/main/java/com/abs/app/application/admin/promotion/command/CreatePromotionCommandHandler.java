package com.abs.app.application.admin.promotion.command;

import com.abs.app.application.admin.promotion.dto.PromotionResponseDto;
import com.abs.app.common.constant.PromotionConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.common.util.GenerateIdUtil;
import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.PromotionStatus;
import com.abs.app.domain.repository.PromotionRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.PromotionService;
import com.abs.app.infrastructure.file.FileStorageService;
import com.abs.app.infrastructure.mapper.PromotionMapper;
import lombok.RequiredArgsConstructor;
import com.abs.app.common.constant.Messages;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePromotionCommandHandler {

    private final PromotionRepository promotionRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final PromotionService promotionService;

    public PromotionResponseDto handle(CreatePromotionCommand command) {
        if (promotionService.handlePromotionDate(command.getStartDate(), command.getEndDate())) {
            throw new BusinessException(Messages.INVALID_DATE);
        }
        Promotion promotion = new Promotion();
        promotion.setId(PromotionConstant.SALT_TAG + command.getPromotionCode());
        promotion.setDescription(command.getDescription());
        promotion.setDiscountAmount(command.getDiscountAmount());
        promotion.setDiscountType(command.getDiscountType());
        promotion.setStatus(PromotionStatus.ACTIVE);
        promotion.setStartDate(command.getStartDate());
        promotion.setEndDate(command.getEndDate());

        if (command.getImage() != null && !command.getImage().isEmpty()) {
            String storedImagePath = fileStorageService.storePromotion(
                    command.getImage(),
                    PromotionConstant.SALT_TAG);
            promotion.setImage(storedImagePath);
        }

        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));
        promotion.setUser(user);

        promotionRepository.save(promotion);
        return PromotionMapper.toPromotionResponse(promotion);
    }

}
