package com.abs.app.application.admin.promotion.command;

import com.abs.app.common.constant.PromotionConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.entity.enums.PromotionStatus;
import com.abs.app.domain.repository.PromotionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletePromotionCommandHandlerTest {

    @Mock
    private PromotionRepository promotionRepository;

    @InjectMocks
    private DeletePromotionCommandHandler handler;

    private Promotion promotion;

    @BeforeEach
    void setUp() {
        promotion = new Promotion();
        promotion.setId("promo-123");
        promotion.setStatus(PromotionStatus.ACTIVE);
    }

    @Test
    void handle_ValidId_DeletesPromotionSuccessfully() {
        // Arrange
        when(promotionRepository.findById("promo-123")).thenReturn(Optional.of(promotion));

        // Act
        handler.handle("promo-123");

        // Assert
        assertEquals(PromotionStatus.DELETED, promotion.getStatus());
        verify(promotionRepository, times(1)).findById("promo-123");
        verify(promotionRepository, times(1)).save(promotion);
    }

    @Test
    void handle_PromotionNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        when(promotionRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> handler.handle("invalid-id"));
        assertEquals(PromotionConstant.NOT_EXIST, exception.getMessage());
        verify(promotionRepository, times(1)).findById("invalid-id");
        verify(promotionRepository, never()).save(any(Promotion.class));
    }
}
