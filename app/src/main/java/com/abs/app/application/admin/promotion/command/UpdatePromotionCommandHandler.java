package com.abs.app.application.admin.promotion.command;

import com.abs.app.application.admin.promotion.dto.PromotionResponseDto;
import com.abs.app.common.constant.Messages;
import com.abs.app.common.constant.PromotionConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.DuplicateResourceException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.DiscountType;
import com.abs.app.domain.entity.enums.PromotionStatus;
import com.abs.app.domain.repository.PromotionRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.PromotionService;
import com.abs.app.infrastructure.file.FileStorageService;
import com.abs.app.infrastructure.mapper.PromotionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdatePromotionCommandHandler {
    private final PromotionRepository promotionRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final PromotionService promotionService;

    public PromotionResponseDto handle(UpdatePromotionCommand command) {
        PromotionStatus promotionStatus = promotionService.convertPromotionStatusToEnum(command.getStatus());
        DiscountType promotionType = promotionService.convertDiscountTypeStringToEnum(command.getDiscountType());
        if (promotionService.handlePromotionDate(command.getStartDate(), command.getEndDate())) {
            throw new BusinessException(Messages.INVALID_DATE);
        }
        Promotion promotion = promotionRepository.findById(command.getId())
                .orElseThrow(() -> new ResourceNotFoundException(PromotionConstant.NOT_EXIST));

        List<Promotion> promotionList = promotionRepository.findAll().stream()
                .filter(exist -> exist.getCode().equals(command.getPromotionCode()) && !exist.getId().equals(command.getId()))
                .toList();
        if (promotionService.handlePromotionDuplicateValid(promotionList, command.getStartDate(), command.getEndDate())) {
            throw new DuplicateResourceException(PromotionConstant.PROMOTION_DATE_OVERLAPPED);
        }

        promotion.setCode(command.getPromotionCode());
        promotion.setDescription(command.getDescription());
        promotion.setDiscountAmount(command.getDiscountAmount());
        promotion.setDiscountType(promotionType);
        promotion.setStatus(promotionStatus);
        promotion.setStartDate(command.getStartDate());
        promotion.setEndDate(command.getEndDate());

        if (command.getImage() != null && !command.getImage().isEmpty()) {
            String storedImagePath = fileStorageService.storePromotion(
                    command.getImage(),
                    promotion.getId());
            promotion.setImage(storedImagePath);
        }

        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));
        promotion.setUser(user);
        promotionRepository.save(promotion);
        return PromotionMapper.toPromotionResponse(promotion);
    }
}
