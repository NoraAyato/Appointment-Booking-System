package com.abs.app.unit.application.admin.category.command;

import com.abs.app.application.admin.category.command.DeleteCategoryCommandHandler;
import com.abs.app.common.exception.ResourceNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryCommandHandlerTest {

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private DeleteCategoryCommandHandler deleteCategoryCommandHandler;

    private Category mockCategory;

    private static final String ID = "Ca-123456";

    @BeforeEach
    void setup() {
        mockCategory = new Category();
        mockCategory.setId(ID);
    }
    @Nested
    @DisplayName("Trường hợp xóa category thành công")
    class SuccessCase {

        @Test
        @DisplayName("Xóa category thành công khi id tồn tại")
        void shouldDeleteCategorySuccessfully_WhenIdIsExist() {
            when(categoryRepository.findById(ID)).thenReturn(Optional.of(mockCategory));

            deleteCategoryCommandHandler.handle(ID);

            verify(categoryRepository, times(1)).findById(ID);
            verify(categoryRepository, times(1)).deleteById(ID);
        }
    }

    @Nested
    @DisplayName("Trường hợp ném ngoại lệ")
    class ExceptionCase {
        @Test
        @DisplayName("Ném ngoại lệ khi id không tồn tại")
        void shouldThrowException_WhenIdNotFound() {
            when(categoryRepository.findById(ID)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> {
                deleteCategoryCommandHandler.handle(ID);
            });

            verify(categoryRepository, times(1)).findById(ID);
            verify(categoryRepository, never()).deleteById(ID);
        }
    }
}
