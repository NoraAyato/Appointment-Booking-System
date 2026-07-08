package com.abs.app.unit.application.admin.category.command;

import com.abs.app.application.admin.category.command.UpdateCategoryCommand;
import com.abs.app.application.admin.category.command.UpdateCategoryCommandHandler;
import com.abs.app.application.admin.category.dto.CategoryResponseDto;
import com.abs.app.common.exception.DuplicateResourceException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.Category;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.repository.CategoryRepository;
import jakarta.persistence.Id;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryCommandHandlerTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private UpdateCategoryCommandHandler updateCategoryCommandHandler;

    private UpdateCategoryCommand mockCommand;

    private Category mockCategory;

    private static final String ID = "CA-123456";
    private static final String NAME = "Massage";
    private static final String DESCRIPTION = "Description";
    private static final String TAG_COLOR = "#FF5733";

    @BeforeEach
    void setup() {
        mockCommand = new UpdateCategoryCommand(ID, TAG_COLOR, NAME, DESCRIPTION);
        mockCategory = new Category();
        mockCategory.setId(ID);
        mockCategory.setName(NAME);
        mockCategory.setDescription(DESCRIPTION);
        mockCategory.setTagColor(TAG_COLOR);
    }

    @Nested
    @DisplayName("Trường hơp cập nhật thành công")
    class SuccessCase {
        @Test
        @DisplayName("Category cập nhật thành công khi giữ nguyên tên gốc")
        void shouldUpdateSuccessfully_WhenKeepingOldName() {
            // ARRANGE
            when(categoryRepository.findById(ID)).thenReturn(Optional.of(mockCategory));
            when(categoryRepository.findByName(NAME)).thenReturn(Optional.of(mockCategory));
            when(categoryRepository.save(any(Category.class))).thenReturn(mockCategory);

            // ACT
            CategoryResponseDto dto = updateCategoryCommandHandler.handle(mockCommand);

            // ASSERT
            assertNotNull(dto);
            verify(categoryRepository, times(1)).findById(ID);
            ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
            verify(categoryRepository, times(1)).save(categoryCaptor.capture());
            Category saveCategory = categoryCaptor.getValue();
            verify(categoryRepository, times(1)).findByName(saveCategory.getName());

            assertNotNull(saveCategory);
            assertEquals(ID, saveCategory.getId());
            assertEquals(NAME, saveCategory.getName());
        }

        @Test
        @DisplayName("Category cập nhật thành công khi sửa tên khác")
        void shouldUpdateSuccessfully_WhenUsingBrandNewName() {
            when(categoryRepository.findById(ID)).thenReturn(Optional.of(mockCategory));
            when(categoryRepository.findByName(NAME)).thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class))).thenReturn(mockCategory);

            CategoryResponseDto dto = updateCategoryCommandHandler.handle(mockCommand);

            assertThat(dto).isNotNull();
            verify(categoryRepository, times(1)).findById(ID);
            verify(categoryRepository, times(1)).findByName(NAME);
            verify(categoryRepository, times(1)).save(any(Category.class));

        }
    }

    @Nested
    @DisplayName("Trường hợp ném ngoại lệ")
    class ExceptionCase {
        @Test
        @DisplayName("Id của Category không tồn tại")
        void shouldThrowException_WhenIdNotFound() {
            when(categoryRepository.findById(ID)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> {
                updateCategoryCommandHandler.handle(mockCommand);
            });

            verify(categoryRepository, times(1)).findById(ID);
            verify(categoryRepository, never()).save(mockCategory);
        }

        @Test
        @DisplayName("Lỗi trùng tên category")
        void shouldThrowException_WhenNameIsDuplicated() {
            when(categoryRepository.findById(ID)).thenReturn(Optional.of(mockCategory));
            Category test = new Category();
            test.setId("Ca-0000");
            test.setName(NAME);
            test.setDescription("DESCRIPTION-1");
            when(categoryRepository.findByName(NAME)).thenReturn(Optional.of(test));

            assertThrows(DuplicateResourceException.class, () -> {
                updateCategoryCommandHandler.handle(mockCommand);
            });

            verify(categoryRepository, times(1)).findById(ID);
            verify(categoryRepository, times(1)).findByName(NAME);
            verify(categoryRepository, never()).save(any(Category.class));
        }
    }
}
