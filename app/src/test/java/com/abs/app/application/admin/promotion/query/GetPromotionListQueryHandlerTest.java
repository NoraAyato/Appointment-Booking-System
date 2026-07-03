package com.abs.app.application.admin.promotion.query;

import com.abs.app.application.admin.promotion.dto.PromotionResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.PromotionStatus;
import com.abs.app.domain.repository.PromotionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPromotionListQueryHandlerTest {

    @Mock
    private PromotionRepository promotionRepository;

    @InjectMocks
    private GetPromotionListQueryHandler handler;

    private Promotion promotion1;
    private Promotion promotion2;

    @BeforeEach
    void setUp() {
        User mockUser = new User();
        mockUser.setUserId("user123");

        promotion1 = new Promotion();
        promotion1.setId("promo-1");
        promotion1.setDescription("Giảm giá 10%");
        promotion1.setStatus(PromotionStatus.ACTIVE);
        promotion1.setDiscountType(com.abs.app.domain.entity.enums.DiscountType.PERCENTAGE);
        promotion1.setStartDate(LocalDate.now().minusDays(5));
        promotion1.setEndDate(LocalDate.now().plusDays(5));
        promotion1.setUser(mockUser);

        promotion2 = new Promotion();
        promotion2.setId("promo-2");
        promotion2.setDescription("Khuyến mãi Tết");
        promotion2.setStatus(PromotionStatus.INACTIVE);
        promotion2.setDiscountType(com.abs.app.domain.entity.enums.DiscountType.FIXED_AMOUNT);
        promotion2.setStartDate(LocalDate.now().plusDays(10));
        promotion2.setEndDate(LocalDate.now().plusDays(20));
        promotion2.setUser(mockUser);
    }

    @Test
    void handle_WithNoFilters_ReturnsAllPromotions() {
        // Arrange
        when(promotionRepository.findAll()).thenReturn(Arrays.asList(promotion1, promotion2));
        GetPromotionListQuery query = new GetPromotionListQuery(null, null, null, null, 1, 10);

        // Act
        PageResponse<PromotionResponseDto> response = handler.handle(query);

        // Assert
        assertEquals(2, response.getTotal());
        assertEquals(2, response.getItems().size());
    }

    @Test
    void handle_WithKeywordFilter_ReturnsFilteredPromotions() {
        // Arrange
        when(promotionRepository.findAll()).thenReturn(Arrays.asList(promotion1, promotion2));
        GetPromotionListQuery query = new GetPromotionListQuery("Tết", null, null, null, 1, 10);

        // Act
        PageResponse<PromotionResponseDto> response = handler.handle(query);

        // Assert
        assertEquals(1, response.getTotal());
    }

    @Test
    void handle_WithStatusFilter_ReturnsFilteredPromotions() {
        // Arrange
        when(promotionRepository.findAll()).thenReturn(Arrays.asList(promotion1, promotion2));
        GetPromotionListQuery query = new GetPromotionListQuery(null, "ACTIVE", null, null, 1, 10);

        // Act
        PageResponse<PromotionResponseDto> response = handler.handle(query);

        // Assert
        assertEquals(1, response.getTotal());
    }
}
