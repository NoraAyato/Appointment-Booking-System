package com.abs.app.unit.application.admin.category.command;

import com.abs.app.application.admin.category.command.CreateCategoryCommand;
import com.abs.app.application.admin.category.command.CreateCategoryCommandHandler;
import com.abs.app.application.admin.category.dto.CategoryResponseDto;
import com.abs.app.common.exception.DuplicateResourceException;
import com.abs.app.domain.entity.Category;
import com.abs.app.domain.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryCommandHandlerTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CreateCategoryCommandHandler createCategoryCommandHandler;

    private CreateCategoryCommand mockCommand;

    private Category mockCategory;

    private static final String ID = "CA_123456";
    private static final String NAME = "Massage";
    private static final String DESCRIPTION = "Giúp giải tỏa căng thẳng";
    private static final String TAG_COLOR = "#FF5733";

    @BeforeEach
    void setup() {
        mockCommand = new CreateCategoryCommand(TAG_COLOR, NAME, DESCRIPTION);

        mockCategory = new Category();
        mockCategory.setId(ID);
        mockCategory.setName(NAME);
        mockCategory.setDescription(DESCRIPTION);
        mockCategory.setTagColor(TAG_COLOR);
    }

    @Nested
    @DisplayName("Trường hợp tạo category thành công")
    class SuccessCase {
        @Test
        @DisplayName("Tạo Category thành công khi tên chưa tồn tại")
        void shouldCreateCategorySuccessfully_WhenNameNotFound() {
            // ARRANGE - GIVEN
            when(categoryRepository.findByName(NAME)).thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class))).thenReturn(mockCategory);

            // ACT - WHEN
            CategoryResponseDto responseDto = createCategoryCommandHandler.handle(mockCommand);

            // ASSERT - THEN
            assertNotNull(responseDto);
            assertEquals(ID, responseDto.getId());
            assertEquals(NAME, responseDto.getName());
            assertEquals(DESCRIPTION, responseDto.getDescription());

            verify(categoryRepository, times(1)).findByName(NAME);
            verify(categoryRepository, times(1)).save(any(Category.class));
        }

    }

    @Nested
    @DisplayName("Trường hợp ném ngoại lệ")
    class ExceptionCase {
        @Test
        @DisplayName("Quăng exception khi tên category đã tồn tại")
        void shouldThrowException_WhenNameIsExist() {
            // ARRANGE
            when(categoryRepository.findByName(NAME)).thenReturn(Optional.of(mockCategory));
            // ACT
            assertThrows(DuplicateResourceException.class, () -> {
                createCategoryCommandHandler.handle(mockCommand);
            });
            // ASSERT
            verify(categoryRepository, times(1)).findByName(NAME);
            verify(categoryRepository, never()).save(mockCategory);
        }
    }

}
