package com.abs.app.unit.application.admin.service.command;

import com.abs.app.application.admin.service.command.CreateServiceCommand;
import com.abs.app.application.admin.service.command.CreateServiceCommandHandler;
import com.abs.app.application.admin.service.dto.ServiceResponseDto;
import com.abs.app.common.exception.DuplicateResourceException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.Category;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.repository.CategoryRepository;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.infrastructure.file.FileStorageService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CreateServiceCommandHandlerTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private CreateServiceCommandHandler createServiceCommandHandler;

    private CreateServiceCommand mockCommand;

    private ServiceEntity mockServiceEntity;

    private Category mocCategory;

    private static final String NAME = "MASSAGE GỌI ĐẦU TEST";
    private static final String DESCRIPTION = "TEST";
    private static final Integer DURATION_MINUTES = 60;
    private static final Double PRICE = 499000.0;
    private static final String CATEGORY_ID = "Ca_32423432";
    private static final List<MultipartFile> IMAGES = new ArrayList<>();

    @BeforeEach
    void setup() {
        mockCommand = new CreateServiceCommand(NAME, DESCRIPTION, DURATION_MINUTES, PRICE, CATEGORY_ID, IMAGES);

        mocCategory = new Category();
        mocCategory.setId(CATEGORY_ID);

        mockServiceEntity = new ServiceEntity();
        mockServiceEntity.setName(NAME);
        mockServiceEntity.setDescription(DESCRIPTION);
        mockServiceEntity.setDurationMinutes(DURATION_MINUTES);
        mockServiceEntity.setPrice(PRICE);
        mockServiceEntity.setCategory(mocCategory);
    }

    @Nested
    @DisplayName("Trường hợp tạo service thành công")
    class SuccessCase {

        @Test
        @DisplayName("Tạo service thành công khi image là rỗng")
        void shouldCreateServiceSuccessfully_WhenImageIsNull() {
            when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(mocCategory));
            when(serviceRepository.existsByNameIgnoreCase(NAME)).thenReturn(false);
            when(serviceRepository.save(any(ServiceEntity.class))).thenReturn(mockServiceEntity);

            ServiceResponseDto dto = createServiceCommandHandler.handle(mockCommand);

            assertThat(dto).isNotNull();
            verify(categoryRepository, times(1)).findById(CATEGORY_ID);
            verify(serviceRepository, times(1)).existsByNameIgnoreCase(NAME);

            ArgumentCaptor<ServiceEntity> serviceCaptor = ArgumentCaptor.forClass(ServiceEntity.class);
            verify(serviceRepository, times(1)).save(serviceCaptor.capture());
            ServiceEntity saveServiceEntity = serviceCaptor.getValue();

            assertThat(saveServiceEntity.getName()).isEqualTo(NAME);
            assertThat(saveServiceEntity.getDescription()).isEqualTo(DESCRIPTION);
            assertThat(saveServiceEntity.getDurationMinutes()).isEqualTo(DURATION_MINUTES);
            assertThat(saveServiceEntity.getPrice()).isEqualTo(PRICE);
            assertThat(saveServiceEntity.getCategory().getId()).isEqualTo(CATEGORY_ID);
            assertThat(saveServiceEntity.getServiceImage()).isNullOrEmpty();
        }

        @Test
        @DisplayName("Tạo service thành công với image")
        void shouldCreateServiceSuccessfully_WithImage() {

            String path1 = "/images/uploads/avatars/Ca_img123";
            String path2 = "/images/uploads/avatars/Ca_img321";
            when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(mocCategory));
            when(serviceRepository.existsByNameIgnoreCase(NAME)).thenReturn(false);
            when(serviceRepository.save(any(ServiceEntity.class))).thenReturn(mockServiceEntity);

            // giả lập file được tải lên
            MultipartFile img1 = mock(MultipartFile.class);
            MultipartFile img2 = mock(MultipartFile.class);
            List<MultipartFile> FAKE_IMAGE = List.of(img1, img2);

            // quăng list FAKE_IMAGE vào command
            CreateServiceCommand command = new CreateServiceCommand(
                    NAME, DESCRIPTION, DURATION_MINUTES, PRICE, CATEGORY_ID, FAKE_IMAGE
            );

            // cái này giả định hàm storeService nhận image, SALT_TAG phải return về path image
            when(fileStorageService.storeService(eq(img1), anyString())).thenReturn(path1);
            when(fileStorageService.storeService(eq(img2), anyString())).thenReturn(path2);

            ServiceResponseDto dto = createServiceCommandHandler.handle(command);

            assertThat(dto).isNotNull();
            verify(categoryRepository, times(1)).findById(CATEGORY_ID);
            verify(serviceRepository, times(1)).existsByNameIgnoreCase(NAME);

            verify(fileStorageService, times(1)).storeService(eq(img1), anyString());
            verify(fileStorageService, times(1)).storeService(eq(img2), anyString());

            ArgumentCaptor<ServiceEntity> serviceCaptor = ArgumentCaptor.forClass(ServiceEntity.class);
            verify(serviceRepository, times(1)).save(serviceCaptor.capture());
            ServiceEntity saveServiceEntity = serviceCaptor.getValue();

            assertThat(saveServiceEntity.getName()).isEqualTo(NAME);
            assertThat(saveServiceEntity.getDescription()).isEqualTo(DESCRIPTION);
            assertThat(saveServiceEntity.getDurationMinutes()).isEqualTo(DURATION_MINUTES);
            assertThat(saveServiceEntity.getPrice()).isEqualTo(PRICE);
            assertThat(saveServiceEntity.getCategory().getId()).isEqualTo(CATEGORY_ID);

            assertThat(saveServiceEntity.getServiceImage()).hasSize(2);
            assertThat(saveServiceEntity.getServiceImage().getFirst().getPicture()).isEqualTo(path1);
            assertThat(saveServiceEntity.getServiceImage().getFirst().getIsMainImage()).isTrue();
            assertThat(saveServiceEntity.getServiceImage().get(1).getPicture()).isEqualTo(path2);
            assertThat(saveServiceEntity.getServiceImage().get(1).getIsMainImage()).isFalse();
        }
    }

    @Nested
    @DisplayName("Trường hợp ném ngoại lệ")
    class ExceptionCase {

        @Test
        @DisplayName("Ném exception khi tạo mới service trùng tên")
        void shouldThrowException_WhenDuplicateName() {
            when(serviceRepository.existsByNameIgnoreCase(NAME)).thenReturn(true);

            assertThatThrownBy(() -> createServiceCommandHandler.handle(mockCommand))
                    .isInstanceOf(DuplicateResourceException.class);

            verify(serviceRepository, times(1)).existsByNameIgnoreCase(NAME);
            verify(categoryRepository, never()).findById(anyString());
            verify(serviceRepository, never()).save(mockServiceEntity);
        }

        @Test
        @DisplayName("Ném exception khi không tìm thấy categoty id")
        void shouldThrowException_WhenCategoryIdNotFound() {
            when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> createServiceCommandHandler.handle(mockCommand))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(categoryRepository, times(1)).findById(CATEGORY_ID);
            verify(serviceRepository, never()).save(mockServiceEntity);
        }
    }
}
