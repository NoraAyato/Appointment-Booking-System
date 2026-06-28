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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdatePromotionCommandHandlerTest {

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private UpdatePromotionCommandHandler handler;

    private UpdatePromotionCommand validCommand;
    private Promotion mockPromotion;
    private User mockUser;

    @BeforeEach
    void setUp() {
        validCommand = new UpdatePromotionCommand(
                "promo-id-1",
                "PROMO2023",
                "Updated Promo",
                15.0,
                "PERCENTAGE",
                "ACTIVE",
                null,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                "user123"
        );

        mockUser = new User();
        mockUser.setUserId("user123");

        mockPromotion = new Promotion();
        mockPromotion.setId("promo-id-1");
        mockPromotion.setCode("PROMO2023");
    }

    @Test
    void handle_ValidCommand_UpdatesPromotionSuccessfully() {
        when(promotionService.convertPromotionStatusToEnum(anyString())).thenReturn(PromotionStatus.ACTIVE);
        when(promotionService.convertDiscountTypeStringToEnum(anyString())).thenReturn(DiscountType.PERCENTAGE);
        when(promotionService.isInvalidPromotionDate(any(), any())).thenReturn(false);
        when(promotionRepository.findById("promo-id-1")).thenReturn(Optional.of(mockPromotion));
        when(promotionRepository.findAll()).thenReturn(Collections.emptyList());
        when(promotionService.isPromotionDateOverlapped(anyList(), any(), any())).thenReturn(false);
        when(userRepository.findById("user123")).thenReturn(Optional.of(mockUser));
        when(promotionRepository.save(any(Promotion.class))).thenAnswer(i -> i.getArguments()[0]);

        PromotionResponseDto response = handler.handle(validCommand);

        assertNotNull(response);
        verify(promotionRepository, times(1)).save(any(Promotion.class));
    }

    @Test
    void handle_InvalidDate_ThrowsBusinessException() {
        when(promotionService.convertPromotionStatusToEnum(anyString())).thenReturn(PromotionStatus.ACTIVE);
        when(promotionService.convertDiscountTypeStringToEnum(anyString())).thenReturn(DiscountType.PERCENTAGE);
        when(promotionService.isInvalidPromotionDate(any(), any())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> handler.handle(validCommand));
        assertEquals(Messages.INVALID_DATE, exception.getMessage());
    }

    @Test
    void handle_PromotionNotFound_ThrowsResourceNotFoundException() {
        when(promotionService.convertPromotionStatusToEnum(anyString())).thenReturn(PromotionStatus.ACTIVE);
        when(promotionService.convertDiscountTypeStringToEnum(anyString())).thenReturn(DiscountType.PERCENTAGE);
        when(promotionService.isInvalidPromotionDate(any(), any())).thenReturn(false);
        when(promotionRepository.findById(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> handler.handle(validCommand));
        assertEquals(PromotionConstant.NOT_EXIST, exception.getMessage());
    }

    @Test
    void handle_OverlappingPromotionDate_ThrowsDuplicateResourceException() {
        when(promotionService.convertPromotionStatusToEnum(anyString())).thenReturn(PromotionStatus.ACTIVE);
        when(promotionService.convertDiscountTypeStringToEnum(anyString())).thenReturn(DiscountType.PERCENTAGE);
        when(promotionService.isInvalidPromotionDate(any(), any())).thenReturn(false);
        when(promotionRepository.findById("promo-id-1")).thenReturn(Optional.of(mockPromotion));
        when(promotionRepository.findAll()).thenReturn(List.of(mockPromotion));
        when(promotionService.isPromotionDateOverlapped(anyList(), any(), any())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> handler.handle(validCommand));
        assertEquals(PromotionConstant.PROMOTION_DATE_OVERLAPPED, exception.getMessage());
    }

    @Test
    void handle_UserNotFound_ThrowsResourceNotFoundException() {
        when(promotionService.convertPromotionStatusToEnum(anyString())).thenReturn(PromotionStatus.ACTIVE);
        when(promotionService.convertDiscountTypeStringToEnum(anyString())).thenReturn(DiscountType.PERCENTAGE);
        when(promotionService.isInvalidPromotionDate(any(), any())).thenReturn(false);
        when(promotionRepository.findById("promo-id-1")).thenReturn(Optional.of(mockPromotion));
        when(promotionRepository.findAll()).thenReturn(Collections.emptyList());
        when(promotionService.isPromotionDateOverlapped(anyList(), any(), any())).thenReturn(false);
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> handler.handle(validCommand));
        assertEquals(UserConstant.USER_NOT_EXIST, exception.getMessage());
    }

    @Test
    void handle_WithImage_UpdatesPromotionAndStoresImage() {
        MockMultipartFile mockImage = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test data".getBytes());
        UpdatePromotionCommand commandWithImage = new UpdatePromotionCommand(
                "promo-id-1",
                "PROMO2023",
                "Updated Promo",
                15.0,
                "PERCENTAGE",
                "ACTIVE",
                mockImage,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                "user123"
        );

        when(promotionService.convertPromotionStatusToEnum(anyString())).thenReturn(PromotionStatus.ACTIVE);
        when(promotionService.convertDiscountTypeStringToEnum(anyString())).thenReturn(DiscountType.PERCENTAGE);
        when(promotionService.isInvalidPromotionDate(any(), any())).thenReturn(false);
        when(promotionRepository.findById("promo-id-1")).thenReturn(Optional.of(mockPromotion));
        when(promotionRepository.findAll()).thenReturn(Collections.emptyList());
        when(promotionService.isPromotionDateOverlapped(anyList(), any(), any())).thenReturn(false);
        when(fileStorageService.storePromotion(any(), anyString())).thenReturn("path/to/image.jpg");
        when(userRepository.findById("user123")).thenReturn(Optional.of(mockUser));
        when(promotionRepository.save(any(Promotion.class))).thenAnswer(i -> i.getArguments()[0]);

        PromotionResponseDto response = handler.handle(commandWithImage);

        assertNotNull(response);
        verify(fileStorageService, times(1)).storePromotion(any(), eq("promo-id-1"));
        verify(promotionRepository, times(1)).save(any(Promotion.class));
    }
}
