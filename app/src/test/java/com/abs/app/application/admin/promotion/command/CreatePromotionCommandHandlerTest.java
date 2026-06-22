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
public class CreatePromotionCommandHandlerTest {

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private CreatePromotionCommandHandler handler;

    private CreatePromotionCommand validCommand;
    private User mockUser;

    @BeforeEach
    void setUp() {
        validCommand = new CreatePromotionCommand(
                "PROMO2023",
                "New Year Promo",
                10.0,
                "PERCENTAGE",
                null,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                "user123"
        );

        mockUser = new User();
        mockUser.setUserId("user123");
    }

    @Test
    void handle_ValidCommand_CreatesPromotionSuccessfully() {
        // Arrange
        when(promotionService.handlePromotionDate(any(), any())).thenReturn(false);
        when(promotionService.convertDiscountTypeStringToEnum(anyString())).thenReturn(DiscountType.PERCENTAGE);
        when(promotionRepository.findAll()).thenReturn(Collections.emptyList());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(mockUser));
        when(promotionRepository.save(any(Promotion.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        PromotionResponseDto response = handler.handle(validCommand);

        // Assert
        assertNotNull(response);
        // removed code assertion
        verify(promotionRepository, times(1)).save(any(Promotion.class));
    }

    @Test
    void handle_InvalidDate_ThrowsBusinessException() {
        // Arrange
        when(promotionService.handlePromotionDate(any(), any())).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> handler.handle(validCommand));
        assertEquals(Messages.INVALID_DATE, exception.getMessage());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    void handle_OverlappingPromotion_ThrowsDuplicateResourceException() {
        // Arrange
        when(promotionService.handlePromotionDate(any(), any())).thenReturn(false);
        when(promotionService.convertDiscountTypeStringToEnum(anyString())).thenReturn(DiscountType.PERCENTAGE);

        Promotion existingPromotion = new Promotion();
        existingPromotion.setCode("PROMO2023");
        existingPromotion.setStartDate(LocalDate.now().plusDays(2));
        existingPromotion.setEndDate(LocalDate.now().plusDays(5));

        when(promotionRepository.findAll()).thenReturn(List.of(existingPromotion));
        when(promotionService.handlePromotionDuplicateValid(any(), any(), any())).thenReturn(true);

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> handler.handle(validCommand));
        assertEquals(PromotionConstant.PROMOTION_DATE_OVERLAPPED, exception.getMessage());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    void handle_UserNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        when(promotionService.handlePromotionDate(any(), any())).thenReturn(false);
        when(promotionService.convertDiscountTypeStringToEnum(anyString())).thenReturn(DiscountType.PERCENTAGE);
        when(promotionRepository.findAll()).thenReturn(Collections.emptyList());
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> handler.handle(validCommand));
        assertEquals(UserConstant.USER_NOT_EXIST, exception.getMessage());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    void handle_WithImage_CreatesPromotionAndStoresImage() {
        // Arrange
        MockMultipartFile mockImage = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test data".getBytes());
        CreatePromotionCommand commandWithImage = new CreatePromotionCommand(
                "PROMO2023",
                "New Year Promo",
                10.0,
                "PERCENTAGE",
                mockImage,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                "user123"
        );

        when(promotionService.handlePromotionDate(any(), any())).thenReturn(false);
        when(promotionService.convertDiscountTypeStringToEnum(anyString())).thenReturn(DiscountType.PERCENTAGE);
        when(promotionRepository.findAll()).thenReturn(Collections.emptyList());
        when(fileStorageService.storePromotion(any(), anyString())).thenReturn("path/to/image.jpg");
        when(userRepository.findById(anyString())).thenReturn(Optional.of(mockUser));
        when(promotionRepository.save(any(Promotion.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        PromotionResponseDto response = handler.handle(commandWithImage);

        // Assert
        assertNotNull(response);
        verify(fileStorageService, times(1)).storePromotion(any(), anyString());
        verify(promotionRepository, times(1)).save(any(Promotion.class));
    }
}
